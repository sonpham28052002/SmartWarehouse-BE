package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ScheduledService {

  @Autowired
  private TransactionDetailRepository transactionDetailRepository;
  @Autowired
  private InventoryRepository inventoryRepository;

  @Scheduled(cron = "0 36 17 * * *", zone = "Asia/Ho_Chi_Minh")
  public void calculateInventoryDaily() throws IOException {
    LocalDateTime from = LocalDate.now().atStartOfDay();
    LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);
    List<TransactionType> types = List.of(
        TransactionType.EXPORT_FROM_WAREHOUSE,
        TransactionType.INVENTORY,
        TransactionType.IMPORT_FROM_WAREHOUSE,
        TransactionType.WAREHOUSE_TRANSFER,
        TransactionType.IMPORT_FROM_SUPPLIER
    );
    List<Inventory> inventories = inventoryRepository.findInventoriesByTransactionDateAndTypesAndStatus(
        from, to, types, InventoryStatus.ACTIVE);
    System.out.println(inventories.size());
    Path path = Paths.get("data/data_csv.csv");

    List<String> lines = Files.readAllLines(path);
    Map<String, List<Long>> inventoryMap = new LinkedHashMap<>();

    for (int i = 1; i < lines.size(); i++) { // Bỏ header
      String[] parts = lines.get(i).split(",");

      String date = parts[0].trim();
      List<Long> values = new ArrayList<>();

      for (int j = 1; j < parts.length; j++) {
        values.add(Long.parseLong(parts[j].trim()));
      }

      inventoryMap.put(date, values);
    }

    // In thử kết quả
    inventoryMap.forEach((key, value) -> {
      System.out.println(key + " => " + value);
    });
  }

  @Scheduled(cron = "0 59 23 L * *", zone = "Asia/Ho_Chi_Minh")
  public void calculateInventoryMonthly() {
    LocalDate today = LocalDate.now();
    LocalDateTime from = today.withDayOfMonth(1).atStartOfDay();
    LocalDateTime to = today.atTime(LocalTime.MAX);
    List<TransactionType> types = List.of(
        TransactionType.EXPORT_FROM_WAREHOUSE,
        TransactionType.INVENTORY,
        TransactionType.IMPORT_FROM_WAREHOUSE,
        TransactionType.WAREHOUSE_TRANSFER,
        TransactionType.IMPORT_FROM_SUPPLIER
    );
    List<Inventory> inventories = inventoryRepository.findInventoriesByTransactionDateAndTypesAndStatus(
        from, to, types, InventoryStatus.ACTIVE);
    System.out.println(inventories.size());
  }

  public static void main(String[] args) throws IOException {
    Path path = Paths.get("D:\\dockerStudy\\SmartWarehouse-BE\\data_csv.csv");

    Map<String, Map<String, Long>> dataMap = new LinkedHashMap<>(); // Giữ thứ tự
    List<String> dateList = new ArrayList<>();
    List<String> headers;

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      headers = Arrays.asList(reader.readLine().split(","));
      for (int i = 1; i < headers.size(); i++) {
        dataMap.put(headers.get(i), new LinkedHashMap<>());
      }

      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length < 2) {
          continue;
        }

        String date = parts[0];
        dateList.add(date);

        for (int i = 1; i < parts.length; i++) {
          String product = headers.get(i);
          dataMap.get(product).put(date, Long.parseLong(parts[i]));
        }
      }
    }

    // Thêm sản phẩm mới
    String newProduct = "PRO-NEW99";
    Map<String, Long> newProductData = new LinkedHashMap<>();
    for (String date : dateList) {
      newProductData.put(date, 0L); // Giá trị mặc định
    }

    // Thêm tháng hiện tại nếu chưa có
    String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    if (!dateList.contains(currentMonth)) {
      dateList.add(currentMonth);
      for (Map<String, Long> productData : dataMap.values()) {
        productData.put(currentMonth, 0L); // Giá trị mặc định cho sản phẩm cũ
      }
    }
    newProductData.put(currentMonth, 1234L); // Giá trị cho sản phẩm mới
    dataMap.put(newProduct, newProductData);

    // Ghi lại file CSV
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      List<String> newHeaders = new ArrayList<>(headers);
      newHeaders.add(newProduct);
      writer.write(String.join(",", newHeaders));
      writer.newLine();

      for (String date : dateList) {
        StringBuilder line = new StringBuilder(date);
        for (int i = 1; i < newHeaders.size(); i++) {
          String prod = newHeaders.get(i);
          line.append(",").append(dataMap.get(prod).getOrDefault(date, 0L));
        }
        writer.write(line.toString());
        writer.newLine();
      }
    }
  }


}
