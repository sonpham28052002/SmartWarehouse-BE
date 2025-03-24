import sys
import json
import numpy as np
import pandas as pd
from statsmodels.tsa.arima.model import ARIMA
from sklearn.cluster import KMeans

# Đọc dữ liệu từ stdin (Java truyền vào)
input_data = sys.stdin.read()
data = json.loads(input_data)

sales_data = data["sales_data"]
df = pd.DataFrame(sales_data).T
df.columns = [f"Month-{i+1}" for i in range(df.shape[1])]

# Phân cụm bằng KMeans
num_clusters = min(3, len(df))  # Số cụm không thể lớn hơn số sản phẩm
kmeans = KMeans(n_clusters=num_clusters, random_state=42, n_init=10)
clusters = kmeans.fit_predict(df)
df["Cluster"] = clusters

# Chọn nhóm bán chạy nhất
best_cluster = df.groupby("Cluster").sum().sum(axis=1).idxmax()
best_selling_products = df[df["Cluster"] == best_cluster].index.tolist()

# Dự báo bằng ARIMA
forecast_results = {}
for product in best_selling_products:
    sales = df.loc[product, df.columns[:-1]].values
    model = ARIMA(sales, order=(2, 1, 2))
    model_fit = model.fit()
    forecast = model_fit.forecast(steps=3)
    forecast_results[product] = forecast.astype(int).tolist()

# Xuất JSON cho Java
print(json.dumps(forecast_results))
