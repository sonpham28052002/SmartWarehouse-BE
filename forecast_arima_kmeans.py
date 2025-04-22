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

# Tắt tất cả cảnh báo
warnings.simplefilter(action='ignore', category=FutureWarning)
warnings.simplefilter(action='ignore', category=UserWarning)
warnings.simplefilter(action='ignore', category=DeprecationWarning)
sys.stdout.reconfigure(encoding='utf-8')


# Hàm làm tròn số thực thành số nguyên
def round_to_integer(obj):
  if isinstance(obj, float):
    return int(round(obj)) if not np.isnan(obj) and not np.isinf(obj) else obj
  elif isinstance(obj, list):
    return [round_to_integer(item) for item in obj]
  elif isinstance(obj, dict):
    return {key: round_to_integer(value) for key, value in obj.items()}
  return obj


# Đọc dữ liệu từ file CSV
file_path = "data/data_csv.csv"
try:
  df = pd.read_csv(file_path, parse_dates=['date'], index_col='date')
except Exception as e:
  print(json.dumps({"error": f"Lỗi khi đọc file CSV: {str(e)}"}))
  sys.exit(1)

# Đọc danh sách sản phẩm và số tháng dự đoán từ stdin
try:
  input_data = json.load(sys.stdin)
  selected_products = input_data.get("selected_products", [])
  n_periods = input_data.get("n_periods", 1)
except Exception as e:
  print(json.dumps({"error": f"Không thể đọc dữ liệu từ Java: {str(e)}"}))
  sys.exit(1)

# Kiểm tra danh sách sản phẩm hợp lệ
valid_products = [p for p in selected_products if p in df.columns]
if not valid_products:
  print(json.dumps({"error": "Không có sản phẩm hợp lệ để dự báo"}))
  sys.exit(1)

# Danh sách tất cả sản phẩm (các cột trong dữ liệu)
products = [col for col in df.columns]

# Phân cụm sản phẩm bằng K-means
features = []
for product in products:
  sales = df[product].values
  mean_sales = np.mean(sales)
  std_sales = np.std(sales)
  trend = (sales[-1] - sales[0]) / len(sales) if len(sales) > 1 else 0
  features.append([mean_sales, std_sales, trend])

# Chuẩn hóa đặc trưng
scaler = StandardScaler()
features_scaled = scaler.fit_transform(features)

# Áp dụng K-means
n_clusters = 3
kmeans = KMeans(n_clusters=n_clusters, random_state=42)
clusters = kmeans.fit_predict(features_scaled)

# Tạo dictionary để lưu thông tin cụm
product_clusters = {product: cluster for product, cluster in
                    zip(products, clusters)}

# Tính start_date: Nếu dữ liệu không đủ 24 tháng, lấy từ ngày đầu tiên
last_date = df.index[-1]
months_available = len(
    pd.date_range(start=df.index[0], end=last_date, freq='M'))
if months_available >= 24:
  start_date = last_date - pd.DateOffset(months=24)
else:
  start_date = df.index[0]  # Lấy ngày đầu tiên nếu không đủ 24 tháng
start_date = start_date.strftime('%Y-%m-%d')

# Tính train_end_date và test_start_date động
train_end_date = (last_date - pd.DateOffset(months=1)).strftime('%Y-%m-%d')
test_start_date = (
    pd.to_datetime(train_end_date) + pd.DateOffset(months=1)).replace(
    day=1).strftime('%Y-%m-%d')

# Giới hạn dữ liệu từ start_date đến ngày cuối cùng + số tháng dự đoán
end_date = pd.date_range(start=df.index[-1], periods=n_periods + 1, freq='M')[
  -1]
display_dates = pd.date_range(start=start_date, end=end_date, freq='M')

# Dự báo cho các sản phẩm được chọn
forecast_results = {}

for product in valid_products:
  try:
    # Chia dữ liệu thành tập huấn luyện và tập kiểm tra
    train_data = df[product].loc[:train_end_date]
    test_data = df[product].loc[test_start_date:]

    # Kiểm tra xem tập kiểm tra có dữ liệu không
    if len(test_data) == 0:
      test_forecast_values = []
      actual_values = []
      mae = 0.0
      mse = 0.0
      rmse = 0.0
      mape = 0.0
    else:
      # Huấn luyện trên tập huấn luyện với yếu tố vụ mùa
      m = 12
      if product_clusters[product] == 0:
        m = 6
      elif product_clusters[product] == 1:
        m = 12
      elif product_clusters[product] == 2:
        m = 3

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

      # Sử dụng SARIMAX để huấn luyện
      model_full = SARIMAX(
          train_data,
          order=(p, d, q),
          seasonal_order=(P, D, Q, m)
      )
      model_full_fit = model_full.fit(disp=False)

      # Dự đoán trên tập kiểm tra
      test_forecast = model_full_fit.forecast(steps=len(test_data))
      test_forecast_values = test_forecast.tolist()

      # Tính toán các chỉ số đánh giá
      actual_values = test_data.values
      mae = mean_absolute_error(actual_values, test_forecast_values)
      mse = mean_squared_error(actual_values, test_forecast_values)
      rmse = np.sqrt(mse)
      mape = np.mean(np.abs((
                                actual_values - test_forecast_values) / actual_values)) * 100 if not np.any(
          actual_values == 0) else float('inf')

    # Huấn luyện lại trên toàn bộ dữ liệu để dự đoán tương lai
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

    # Lọc dữ liệu lịch sử từ start_date
    historical_data_full = df[product].loc[start_date:].reindex(display_dates,
                                                                fill_value=np.nan).tolist()
    historical_dates = display_dates[
                       :len(df[product].loc[start_date:])].strftime(
        '%Y-%m-%d').tolist()
    historical_values = historical_data_full[:len(historical_dates)]

    # Dữ liệu dự đoán
    forecast_values = future_forecast.tolist()
    future_dates_str = future_dates.strftime('%Y-%m-%d').tolist()

    # Khoảng tin cậy
    ci_lower = conf_int_95_lower.tolist()
    ci_upper = conf_int_95_upper.tolist()

    # Tạo nhận xét chi tiết về độ chính xác và tính hợp lý của dự đoán
    product_review = []
    product_review.append(
        f"Chỉ số đánh giá: MAE: {int(round(mae))}, RMSE: {int(round(rmse))}, MAPE: {int(round(mape))}%.")  # Làm tròn thành số nguyên

    # So sánh giá trị thực tế và dự đoán cho từng tháng
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
      percentage_error = (error / actual * 100) if actual != 0 else float('inf')
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
          f"- Tháng {date}: Thực tế: {int(round(actual))}, Dự đoán: {int(round(pred))}, Sai số: {int(round(error))} ({int(round(percentage_error))}%), {accuracy_comment}, {trend_comment}.")  # Làm tròn thành số nguyên

    product_review.append("Đánh giá độ chính xác dự đoán:")
    product_review.extend(comparison if comparison else [
      "- Không có dữ liệu kiểm tra để đánh giá."])

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

    forecast_results[product] = {
      "cluster": int(product_clusters[product]),
      "historical_data": {
        "dates": historical_dates,
        "values": historical_values
      },
      "forecast": [
        {
          "date": date,
          "value": int(round(value))
        } for date, value in zip(future_dates_str, forecast_values)
      ],
      "confidence_interval_95": {
        "lower": [int(round(val)) for val in ci_lower],
        "upper": [int(round(val)) for val in ci_upper]
      },
      "evaluation": {
        "test_dates": test_data.index.strftime('%Y-%m-%d').tolist(),
        "actual_values": [int(round(val)) for val in test_data.tolist()],
        "predicted_values": [int(round(val)) for val in test_forecast_values],
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
    forecast_results[product] = {"error": f"Lỗi khi dự báo: {str(e)}"}

# Xuất kết quả JSON với tất cả số thực được làm tròn thành số nguyên
forecast_results_rounded = round_to_integer(forecast_results)
print(json.dumps(forecast_results_rounded, ensure_ascii=False))
sys.stdout.flush()
