package vn.edu.iuh.fit.smartwarehousebe.servies;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;

@Service
public class ArimaKMeansService {

  @Autowired
  private ProductRepository productRepository;

  public List<ProductResponse> forecastSales(List<String> selectedProducts) {
    try {

      ObjectMapper objectMapper = new ObjectMapper();
      String jsonString = objectMapper.writeValueAsString(
          Map.of("selected_products", selectedProducts, "n_periods", 2));

      ProcessBuilder pb = new ProcessBuilder("python3", "/app-be/forecast_arima_kmeans.py");
      pb.redirectErrorStream(true);
      Process process = pb.start();

      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
      writer.write(jsonString);
      writer.close();

      BufferedReader reader = new BufferedReader(
          new InputStreamReader(process.getInputStream(), "UTF-8"));
      StringBuilder output = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line);
      }
      reader.close();

      Map<String, Object> result = objectMapper.readValue(output.toString(), Map.class);
      List<ProductResponse> responses = new ArrayList<>();
      for (Map.Entry<String, Object> entry : result.entrySet()) {
        System.out.println(output.toString());
        System.out.println(entry.getKey());
        System.out.println(entry.getValue());

        ProductResponse response = ProductMapper.INSTANCE.toDto(
            productRepository.findByCode(entry.getKey()).get());
        response.setQuantityForecasts((List<Integer>) entry.getValue());
        responses.add(response);
      }
      return responses;

    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }
}