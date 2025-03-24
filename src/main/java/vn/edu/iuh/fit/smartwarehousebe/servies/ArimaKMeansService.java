package vn.edu.iuh.fit.smartwarehousebe.servies;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ArimaKMeansService {

  public static Map<String, Object> forecastSales(Map<String, double[]> salesData) {
    try {
      // Chuyển dữ liệu sang JSON bằng Jackson
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(Map.of("sales_data", salesData));

      // Gọi Python script trong Docker container
      ProcessBuilder pb = new ProcessBuilder("python3", "/app-be/forecast_arima_kmeans.py");
      Process process = pb.start();

      // Gửi dữ liệu vào stdin của Python
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      writer.write(jsonString);
      writer.close();

      // Đọc kết quả từ stdout của Python
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String output = reader.readLine();
      reader.close();

      // Chuyển kết quả từ JSON string sang Map
      return objectMapper.readValue(output, Map.class);

    } catch (Exception e) {
      e.printStackTrace();
      return Collections.emptyMap(); // Trả về map rỗng nếu lỗi
    }
  }
}