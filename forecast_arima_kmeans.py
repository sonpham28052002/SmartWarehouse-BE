import pandas as pd
import numpy as np
from pmdarima import auto_arima
from statsmodels.tsa.statespace.sarimax import SARIMAX
import json
import sys
import warnings
from sklearn.metrics import mean_absolute_error, mean_squared_error
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
import os

# Tắt tất cả cảnh báo
warnings.simplefilter(action='ignore', category=FutureWarning)
warnings.simplefilter(action='ignore', category=UserWarning)
warnings.simplefilter(action='ignore', category=DeprecationWarning)
sys.stdout.reconfigure(encoding='utf-8')


# Hàm làm tròn số thực thành số nguyên, xử lý NaN và inf
def round_to_integer(obj):
  if isinstance(obj, (float, np.floating)):
    return int(round(obj)) if not np.isnan(obj) and not np.isinf(obj) else 0
  elif isinstance(obj, (list, tuple)):
    return [round_to_integer(item) for item in obj]
  elif isinstance(obj, dict):
    return {key: round_to_integer(value) for key, value in obj.items()}
  elif isinstance(obj, (np.ndarray)):
    return [round_to_integer(item) for item in obj.tolist()]
  return obj


# Đọc danh sách sản phẩm và số tháng dự đoán từ stdin
try:
  input_data = json.load(sys.stdin)
  selected_products = input_data.get("selected_products", [])
  n_periods = input_data.get("n_periods", 1)
  warehouse_code = input_data.get("warehouseCode", None)
except Exception as e:
  print(json.dumps({"error": f"Không thể đọc dữ liệu từ Java: {str(e)}"}))
  sys.exit(1)

# Đọc dữ liệu từ file CSV
if warehouse_code:
  file_path = f"data/{warehouse_code}.csv"
else:
  file_path = "data/data_csv.csv"

if not os.path.exists(file_path):
  print(json.dumps({"error": f"Tệp dữ liệu {file_path} không tồn tại"}))
  sys.exit(1)

try:
  df = pd.read_csv(file_path, parse_dates=['date'], index_col='date')
  # Xử lý NaN trong dữ liệu gốc
  df = df.fillna(0)
except Exception as e:
  print(json.dumps({"error": f"Lỗi khi đọc file CSV: {str(e)}"}))
  sys.exit(1)

# Kiểm tra danh sách sản phẩm hợp lệ
valid_products = [p for p in selected_products if p in df.columns]
if not valid_products:
  print(json.dumps({"error": "Không có sản phẩm hợp lệ để dự báo"}))
  sys.exit(1)

# Danh sách tất cả sản phẩm
products = [col for col in df.columns]

# Phân cụm sản phẩm bằng K-means
features = []
for product in products:
  sales = df[product].values
  mean_sales = np.mean(sales)
  std_sales = np.std(sales)
  trend = (sales[-1] - sales[0]) / len(sales) if len(sales) > 1 else 0
  features.append([mean_sales, std_sales, trend])

scaler = StandardScaler()
features_scaled = scaler.fit_transform(features)

n_clusters = 3
kmeans = KMeans(n_clusters=n_clusters, random_state=42)
clusters = kmeans.fit_predict(features_scaled)
product_clusters = {product: cluster for product, cluster in
                    zip(products, clusters)}

# Tính các mốc thời gian
last_date = df.index[-1]
months_available = len(
  pd.date_range(start=df.index[0], end=last_date, freq='M'))
start_date = last_date - pd.DateOffset(months=24) if months_available >= 24 else \
df.index[0]
start_date = start_date.strftime('%Y-%m-%d')

train_end_date = (last_date - pd.DateOffset(months=1)).strftime('%Y-%m-%d')
test_start_date = (
      pd.to_datetime(train_end_date) + pd.DateOffset(months=1)).replace(
  day=1).strftime('%Y-%m-%d')

end_date = pd.date_range(start=df.index[-1], periods=n_periods + 1, freq='M')[
  -1]
display_dates = pd.date_range(start=start_date, end=end_date, freq='M')

# Dự báo cho các sản phẩm
forecast_results = {}

for product in valid_products:
  try:
    # Chia dữ liệu
    train_data = df[product].loc[:train_end_date]
    test_data = df[product].loc[test_start_date:]

    # Kiểm tra tập kiểm tra
    if len(test_data) == 0:
      test_forecast_values = []
      actual_values = []
      mae = 0
      mse = 0
      rmse = 0
      mape = 0
    else:
      # Chọn chu kỳ mùa
      m = 12
      if product_clusters[product] == 0:
        m = 6
      elif product_clusters[product] == 1:
        m = 12
      elif product_clusters[product] == 2:
        m = 3

      # Huấn luyện auto_arima
      model_auto = auto_arima(
          train_data,
          seasonal=True,
          m=m,
          trace=False,
          suppress_warnings=True,
          seasonal_test="ch"
      )
      p, d, q = model_auto.order
      P, D, Q, m = model_auto.seasonal_order

      # Huấn luyện SARIMAX
      model_full = SARIMAX(
          train_data,
          order=(p, d, q),
          seasonal_order=(P, D, Q, m)
      )
      model_full_fit = model_full.fit(disp=False)

      # Dự đoán trên tập kiểm tra
      test_forecast = model_full_fit.forecast(steps=len(test_data))
      test_forecast_values = np.nan_to_num(test_forecast.tolist(), nan=0)

      # Tính toán chỉ số đánh giá
      actual_values = test_data.values
      mae = mean_absolute_error(actual_values, test_forecast_values)
      mse = mean_squared_error(actual_values, test_forecast_values)
      rmse = np.sqrt(mse)
      mape = np.mean(np.abs((
                                  actual_values - test_forecast_values) / actual_values)) * 100 if not np.any(
        actual_values == 0) else 0
      # Xử lý NaN trong chỉ số
      mae = np.nan_to_num(mae, nan=0)
      mse = np.nan_to_num(mse, nan=0)
      rmse = np.nan_to_num(rmse, nan=0)
      mape = np.nan_to_num(mape, nan=0)

    # Huấn luyện lại trên toàn bộ dữ liệu
    model_full = SARIMAX(
        df[product],
        order=(p, d, q),
        seasonal_order=(P, D, Q, m)
    )
    model_full_fit = model_full.fit(disp=False)
    future_forecast = model_full_fit.forecast(steps=n_periods)
    forecast_obj = model_full_fit.get_forecast(steps=n_periods)
    conf_int_95 = forecast_obj.conf_int(alpha=0.05)
    conf_int_95_lower = conf_int_95[f'lower {product}']
    conf_int_95_upper = conf_int_95[f'upper {product}']
    future_dates = pd.date_range(start=df.index[-1], periods=n_periods + 1,
                                 freq='M')[1:]

    # Xử lý NaN trong dự báo và khoảng tin cậy
    forecast_values = np.nan_to_num(future_forecast.tolist(), nan=0)
    ci_lower = np.nan_to_num(conf_int_95_lower.tolist(), nan=0)
    ci_upper = np.nan_to_num(conf_int_95_upper.tolist(), nan=0)

    # Lọc dữ liệu lịch sử
    historical_data_full = df[product].loc[start_date:].reindex(display_dates,
                                                                fill_value=0).tolist()  # Thay np.nan bằng 0
    historical_dates = display_dates[
                       :len(df[product].loc[start_date:])].strftime(
      '%Y-%m-%d').tolist()
    historical_values = historical_data_full[:len(historical_dates)]

    # Dữ liệu dự đoán
    future_dates_str = future_dates.strftime('%Y-%m-%d').tolist()

    # Tạo nhận xét
    product_review = []
    product_review.append(
        f"Chỉ số đánh giá: MAE: {int(round(mae))}, RMSE: {int(round(rmse))}, MAPE: {int(round(mape))}%.")  # Làm tròn thành số nguyên

    test_dates = test_data.index.strftime('%Y-%m-%d').tolist()
    comparison = []
    errors = [abs(actual - pred) for actual, pred in
              zip(actual_values, test_forecast_values)] if len(
      actual_values) > 0 else []
    max_error_idx = errors.index(max(errors)) if errors else 0
    max_error_date = test_dates[max_error_idx] if test_dates else "N/A"
    max_error_actual = actual_values[max_error_idx] if len(
      actual_values) > 0 else 0
    max_error_pred = test_forecast_values[max_error_idx] if len(
      test_forecast_values) > 0 else 0

    for date, actual, pred in zip(test_dates, actual_values,
                                  test_forecast_values):
      error = abs(actual - pred)
      percentage_error = (error / actual * 100) if actual != 0 else 0
      if percentage_error < 2:
        accuracy_comment = "rất chính xác"
      elif percentage_error < 5:
        accuracy_comment = "khá chính xác"
      elif percentage_error < 10:
        accuracy_comment = "chấp nhận được"
      else:
        accuracy_comment = "không chính xác"
      trend_comment = "dự đoán cao hơn thực tế" if pred > actual else "dự đoán thấp hơn thực tế"
      comparison.append(
          f"- Tháng {date}: Thực tế: {int(round(actual))}, Dự đoán: {int(round(pred))}, Sai số: {int(round(error))} ({int(round(percentage_error))}%), {accuracy_comment}, {trend_comment}."
      )

    product_review.append("Đánh giá độ chính xác dự đoán:")
    product_review.extend(comparison if comparison else [
      "- Không có dữ liệu kiểm tra để đánh giá."])

    # Đề xuất phân phối
    dispatch_recommendation = []
    for date, forecast, lower, upper in zip(future_dates_str, forecast_values,
                                            ci_lower, ci_upper):
      adjusted_forecast = forecast
      if errors and max(errors) > 100:
        adjusted_forecast = forecast * 0.95
      dispatch_recommendation.append({
        "date": date,
        "forecast": int(round(forecast)),
        "recommended_dispatch": int(round(adjusted_forecast)),
        "confidence_interval_95": [int(round(lower)), int(round(upper))]
      })

    # Lưu kết quả
    forecast_results[product] = {
      "cluster": int(product_clusters[product]),
      "historical_data": {
        "dates": historical_dates,
        "values": round_to_integer(historical_values)
      },
      "forecast": [
        {
          "date": date,
          "value": int(round(value))
        } for date, value in zip(future_dates_str, forecast_values)
      ],
      "confidence_interval_95": {
        "lower": round_to_integer(ci_lower),
        "upper": round_to_integer(ci_upper)
      },
      "evaluation": {
        "test_dates": test_data.index.strftime('%Y-%m-%d').tolist(),
        "actual_values": round_to_integer(test_data.tolist()),
        "predicted_values": round_to_integer(test_forecast_values),
        "metrics": {
          "MAE": int(round(mae)),
          "MSE": int(round(mse)),
          "RMSE": int(round(rmse)),
          "MAPE": int(round(mape))
        }
      },
      "review": product_review,
      "dispatch_recommendation": dispatch_recommendation
    }

  except Exception as e:
    forecast_results[product] = {
      "error": f"Lỗi khi dự báo cho sản phẩm {product}: {str(e)}",
      "details": {
        "forecast_values_nan": np.isnan(
          forecast_values).any() if 'forecast_values' in locals() else "N/A",
        "ci_lower_nan": np.isnan(
          ci_lower).any() if 'ci_lower' in locals() else "N/A",
        "ci_upper_nan": np.isnan(
          ci_upper).any() if 'ci_upper' in locals() else "N/A",
        "test_data_nan": test_data.isna().any() if 'test_data' in locals() else "N/A",
        "test_forecast_values_nan": np.isnan(
          test_forecast_values).any() if 'test_forecast_values' in locals() else "N/A"
      }
    }

# Xuất kết quả JSON
forecast_results_rounded = round_to_integer(forecast_results)
print(json.dumps(forecast_results_rounded, ensure_ascii=False))
sys.stdout.flush()
