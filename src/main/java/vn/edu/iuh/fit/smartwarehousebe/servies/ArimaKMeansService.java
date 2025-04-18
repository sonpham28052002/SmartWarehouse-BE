package vn.edu.iuh.fit.smartwarehousebe.servies;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;

@Service
public class ArimaKMeansService {

  @Autowired
  private ProductRepository productRepository;

  @Cacheable(value = "forecastSales", key = "#selectedProducts.toArray() + '_' + T(java.time.YearMonth).now().toString()")
  public Map<String, Object> forecastSales(List<String> selectedProducts) {
    // Tạo ExecutorService với số thread cố định (ví dụ: 4)
    ExecutorService executor = Executors.newFixedThreadPool(4);
    // Lưu trữ các Future để thu thập kết quả
    List<Future<Map<String, Object>>> futures = new ArrayList<>();
    ObjectMapper objectMapper = new ObjectMapper();

    try {
      // Tạo task cho từng sản phẩm
      for (String product : selectedProducts) {
        Callable<Map<String, Object>> task = () -> {
          try {
            // Tạo JSON input cho sản phẩm cụ thể
            String jsonString = objectMapper.writeValueAsString(
                Map.of("selected_products", List.of(product), "n_periods", 2));

            // Chạy script Python
            ProcessBuilder pb = new ProcessBuilder(
                "C:\\Users\\Leon\\AppData\\Local\\Programs\\Python\\Python311\\python.exe",
                "D:\\dockerStudy\\SmartWarehouse-BE\\forecast_arima_kmeans.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Gửi JSON input tới script
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
            writer.write(jsonString);
            writer.close();

            // Đọc output từ script
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
              output.append(line);
            }
            reader.close();

            // Parse output thành Map
            return objectMapper.readValue(output.toString(), Map.class);
          } catch (Exception e) {
            e.printStackTrace();
            return null;
          }
        };

        // Gửi task vào ExecutorService
        futures.add(executor.submit(task));
      }

      // Thu thập kết quả từ các task
      Map<String, Object> combinedResult = new HashMap<>();
      for (Future<Map<String, Object>> future : futures) {
        try {
          Map<String, Object> result = future.get(); // Chờ kết quả từ thread
          if (result != null) {
            combinedResult.putAll(result); // Hợp nhất kết quả
          }
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }

      return combinedResult;

    } finally {
      // Đóng ExecutorService
      executor.shutdown();
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
      }
    }
  }

  public Map<Integer, Map<String, Object>> clusterProducts(
      Map<String, Long> productQuantities) {
    Map<DoublePoint, String> pointToProduct = new HashMap<>();
    Map<String, Long> productToQuantity = new HashMap<>();
    List<DoublePoint> points = new ArrayList<>();

    for (Map.Entry<String, Long> entry : productQuantities.entrySet()) {
      DoublePoint point = new DoublePoint(new double[]{entry.getValue()});
      points.add(point);
      pointToProduct.put(point, entry.getKey());
      productToQuantity.put(entry.getKey(), entry.getValue());
    }

    int k = 2;
    if (k > productQuantities.size()) {
      throw new IllegalArgumentException("Số cụm K không thể lớn hơn số lượng sản phẩm.");
    }

    KMeansPlusPlusClusterer<DoublePoint> kMeans = new KMeansPlusPlusClusterer<>(k, 1000);
    List<CentroidCluster<DoublePoint>> clusters = kMeans.cluster(points);

    Map<Integer, Map<String, Object>> result = new HashMap<>();
    int clusterIndex = 1;
    long maxCluster1 = clusters.get(0).getPoints().stream()
        .mapToLong(point -> Math.round(point.getPoint()[0]))
        .max().getAsLong();
    long maxCluster2 = clusters.get(1).getPoints().stream()
        .mapToLong(point -> Math.round(point.getPoint()[0]))
        .max().getAsLong();
    int maxIndex = maxCluster1 >= maxCluster2 ? 1 : 2;
    for (Cluster<DoublePoint> cluster : clusters) {
      Map<String, Object> clusterData = new HashMap<>();
      for (DoublePoint point : cluster.getPoints()) {
        for (Map.Entry<String, Long> entry : productQuantities.entrySet()) {
          long value = Math.round(point.getPoint()[0]);

          if (entry.getValue() == value) {
            String productName = entry.getKey();
            ProductResponse product = ProductMapper.INSTANCE.toDto(
                productRepository.findByCode(productName).get());
            long quantity = value;
            String type = clusterIndex == maxIndex ? "FASTSELLING" : "SLOWSELLING";
            clusterData.put(productName,
                Map.of("product", product, "quantity", quantity, "type", type));
          }
        }
      }
      result.put(clusterIndex++, clusterData);
    }

    return result;
  }

}