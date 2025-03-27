import pandas as pd
import numpy as np
from pmdarima import auto_arima
from statsmodels.tsa.arima.model import ARIMA
import json
import sys
import warnings

# Tắt tất cả cảnh báo
warnings.simplefilter(action='ignore', category=FutureWarning)
warnings.simplefilter(action='ignore', category=UserWarning)
warnings.simplefilter(action='ignore', category=DeprecationWarning)

# Đọc dữ liệu từ file CSV
file_path = "data_csv.csv"
try:
  df = pd.read_csv(file_path, parse_dates=['date'], index_col='date')
except Exception as e:
  print(json.dumps({"error": f"Lỗi khi đọc file CSV: {str(e)}"}))
  sys.exit(1)

# Đọc danh sách sản phẩm từ stdin
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

# Dự báo cho các sản phẩm được chọn
forecast_results = {}

for product in valid_products:  # 🔥 Chỉ chạy trên sản phẩm được chọn
  try:
    # Xác định tham số tự động
    model_auto = auto_arima(df[product], seasonal=False, trace=False,
                            suppress_warnings=True)
    p, d, q = model_auto.order

    # Huấn luyện mô hình ARIMA
    model = ARIMA(df[product], order=(p, d, q))
    model_fit = model.fit()

    # Dự báo n tháng tiếp theo
    future_forecast = model_fit.forecast(steps=n_periods)
    forecast_results[product] = np.ceil(future_forecast).astype(int).tolist()

  except Exception as e:
    forecast_results[product] = f"Error: {str(e)}"

# Xuất kết quả JSON
print(json.dumps(forecast_results))
sys.stdout.flush()
