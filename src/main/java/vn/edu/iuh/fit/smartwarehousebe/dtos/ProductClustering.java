package vn.edu.iuh.fit.smartwarehousebe.dtos;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.util.*;
import java.util.stream.Collectors;

public class ProductClustering {

  public static void main(String[] args) {
    List<TransactionDetail> transactions = generateTransactions(1000);

    Map<String, Double> totalQuantities = transactions.stream()
        .collect(Collectors.groupingBy(TransactionDetail::getProductName,
            Collectors.summingDouble(TransactionDetail::getQuantity)));

    Map<Double, List<String>> productMap = new HashMap<>();
    List<DoublePoint> points = new ArrayList<>();
    for (Map.Entry<String, Double> entry : totalQuantities.entrySet()) {
      double totalQuantity = entry.getValue();
      points.add(new DoublePoint(new double[]{totalQuantity}));
      productMap.computeIfAbsent(totalQuantity, v -> new ArrayList<>()).add(entry.getKey());
    }

    int k = 3;
    if (k > totalQuantities.size()) {
      throw new IllegalArgumentException("Số cụm K không thể lớn hơn số lượng sản phẩm.");
    }

    KMeansPlusPlusClusterer<DoublePoint> kMeans = new KMeansPlusPlusClusterer<>(k, 1000);
    List<CentroidCluster<DoublePoint>> clusters = kMeans.cluster(points);

    System.out.println("\n===== Kết quả phân cụm =====");
    int clusterNumber = 1;
    for (Cluster<DoublePoint> cluster : clusters) {
      System.out.println("\n📦 Cụm " + clusterNumber + " gồm:");
      for (DoublePoint point : cluster.getPoints()) {
        double totalQuantity = point.getPoint()[0];
        List<String> productNames = productMap.get(totalQuantity);
        if (productNames != null) {
          for (String productName : productNames) {
            System.out.println("- " + productName + " | Tổng Quantity: " + totalQuantity);
          }
        }
      }
      clusterNumber++;
    }
  }

  private static List<TransactionDetail> generateTransactions(int count) {
    List<TransactionDetail> transactions = new ArrayList<>();
    Random random = new Random();
    String[] products = {"Product A", "Product B", "Product C", "Product D", "Product E",
        "Product F", "Product G", "Product H", "Product I", "Product J",
        "Product K", "Product L", "Product M", "Product N", "Product O",
        "Product P", "Product Q", "Product R", "Product S", "Product T"};

    for (int i = 0; i < count; i++) {
      String product = products[random.nextInt(products.length)];
      double quantity = random.nextInt(5000) + 50;
      transactions.add(new TransactionDetail(product, quantity));
    }
    return transactions;
  }

  static class TransactionDetail {

    private final String productName;
    private final double quantity;

    public TransactionDetail(String productName, double quantity) {
      this.productName = productName;
      this.quantity = quantity;
    }

    public String getProductName() {
      return productName;
    }

    public double getQuantity() {
      return quantity;
    }
  }
}
