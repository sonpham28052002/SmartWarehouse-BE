package vn.edu.iuh.fit.smartwarehousebe.servies;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ArimaKMeansService {

  public static Map<String, Object> forecastSales(Map<String, double[]> salesData) {
    try {
      List<String> selectedProducts = Arrays.asList("SP00001", "SP00002");

      // Chuyển dữ liệu sang JSON bằng Jackson
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(
          Map.of("selected_products", selectedProducts, "n_periods", 2));

      // Gọi Python script trong Docker container
      ProcessBuilder pb = new ProcessBuilder("python3", "/app-be/forecast_arima_kmeans.py");
      pb.redirectErrorStream(true);
      Process process = pb.start();

      // Gửi dữ liệu vào stdin của Python
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
      writer.write(jsonString);
      writer.close();

      // Đọc kết quả từ stdout của Python
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(process.getInputStream(), "UTF-8"));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }
      reader.close();

      // Chuyển kết quả từ JSON string sang Map
      return objectMapper.readValue(output.toString(), Map.class);

    } catch (Exception e) {
      e.printStackTrace();
      return Collections.emptyMap(); // Trả về map rỗng nếu lỗi
    }
  }
}