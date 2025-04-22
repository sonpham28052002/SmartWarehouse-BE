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
import java.util.concurrent.TimeoutException;
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

  @Autowired
  private ArimaKMeansService self;

  @Cacheable(value = "forecastSales", key = "#selectedProducts.toArray() + '_' + T(java.time.YearMonth).now().toString()")
  public Map<String, Object> forecastSales(List<String> selectedProducts) {
    int cores = Math.min(Runtime.getRuntime().availableProcessors(),
        2);  // Giới hạn luồng an toàn cho Docker
    ExecutorService executor = Executors.newFixedThreadPool(cores);
    List<Future<Map<String, Object>>> futures = new ArrayList<>();

    try {
      for (String product : selectedProducts) {
        futures.add(executor.submit(() -> self.forecast(List.of(product))));
      }

      Map<String, Object> combinedResult = new HashMap<>();
      for (Future<Map<String, Object>> future : futures) {
        try {
          Map<String, Object> result = future.get(120, TimeUnit.SECONDS);  // Timeout 2 phút
          if (result != null) {
            combinedResult.putAll(result);
          }
        } catch (TimeoutException e) {
          System.err.println("Forecast task timed out!");
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
      return combinedResult;

    } finally {
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

  @Cacheable(value = "forecastSales", key = "#selectedProducts.toArray() + '_' + T(java.time.YearMonth).now().toString()")
  public Map<String, Object> forecast(List<String> selectedProducts) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(Map.of(
        "selected_products", selectedProducts,
        "n_periods", 2
    ));

    ProcessBuilder pb = new ProcessBuilder("/usr/bin/python3", "/app-be/forecast_arima_kmeans.py");
    pb.redirectErrorStream(true);
    Process process = pb.start();

    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(process.getOutputStream(), "UTF-8"))) {
      writer.write(jsonString);
      writer.flush();
    }

    // Đọc output riêng một thread để tránh treo
    ExecutorService singleThread = Executors.newSingleThreadExecutor();
    Future<String> outputFuture = singleThread.submit(() -> {
      StringBuilder output = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(process.getInputStream(), "UTF-8"))) {
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line);
        }
      }
      return output.toString();
    });

    int exitCode = process.waitFor();
    String output = outputFuture.get(60, TimeUnit.SECONDS);  // Timeout đọc output

    singleThread.shutdown();

    if (exitCode != 0) {
      throw new RuntimeException(
          "Python script error. Exit code: " + exitCode + ", Output: " + output);
    }

    return objectMapper.readValue(output, Map.class);
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