package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import vn.edu.iuh.fit.smartwarehousebe.servies.ArimaKMeansService;

@RestController
@RequestMapping("/api/forecast")
public class ArimaKMeansController {

  private final ArimaKMeansService arimaKMeansService;

  public ArimaKMeansController(ArimaKMeansService arimaKMeansService) {
    this.arimaKMeansService = arimaKMeansService;
  }

  @PostMapping
  public Map<String, Object> forecast(
      @RequestBody(required = false) Map<String, double[]> salesData) {
    // Nếu không có dữ liệu đầu vào, dùng dữ liệu mẫu
    if (salesData == null || salesData.isEmpty()) {
      salesData = getSampleData();
    }

    return arimaKMeansService.forecastSales(salesData);
  }

  @GetMapping("/sample")
  public Map<String, double[]> getSampleData() {
    Map<String, double[]> sampleData = new HashMap<>();

    sampleData.put("P-1", new double[]{10, 15, 20, 30, 50, 60, 70, 85, 90, 100});
    sampleData.put("P-2", new double[]{5, 8, 12, 18, 25, 35, 50, 65, 80, 95});
    sampleData.put("P-3", new double[]{3, 6, 9, 12, 18, 24, 30, 40, 55, 70});
    sampleData.put("P-4", new double[]{12, 18, 22, 30, 42, 55, 67, 75, 88, 99});
    sampleData.put("P-5", new double[]{8, 16, 24, 32, 48, 56, 64, 72, 80, 96});
    sampleData.put("P-6", new double[]{20, 28, 36, 44, 52, 60, 70, 80, 90, 100});
    sampleData.put("P-7", new double[]{7, 14, 21, 28, 35, 42, 49, 56, 63, 70});
    sampleData.put("P-8", new double[]{25, 30, 35, 40, 50, 60, 70, 85, 90, 100});
    sampleData.put("P-9", new double[]{6, 12, 18, 24, 30, 36, 42, 48, 54, 60});
    sampleData.put("P-10", new double[]{15, 22, 29, 36, 43, 50, 57, 64, 71, 78});
    sampleData.put("P-11", new double[]{10, 18, 26, 34, 42, 50, 58, 66, 74, 82});
    sampleData.put("P-12", new double[]{9, 17, 25, 33, 41, 49, 57, 65, 73, 81});
    sampleData.put("P-13", new double[]{11, 20, 29, 38, 47, 56, 65, 74, 83, 92});
    sampleData.put("P-14", new double[]{13, 23, 33, 43, 53, 63, 73, 83, 93, 103});
    sampleData.put("P-15", new double[]{5, 10, 15, 20, 30, 40, 50, 60, 70, 80});
    sampleData.put("P-16", new double[]{14, 24, 34, 44, 54, 64, 74, 84, 94, 104});
    sampleData.put("P-17", new double[]{16, 26, 36, 46, 56, 66, 76, 86, 96, 106});
    sampleData.put("P-18", new double[]{18, 28, 38, 48, 58, 68, 78, 88, 98, 108});
    sampleData.put("P-19", new double[]{20, 30, 40, 50, 60, 70, 80, 90, 100, 110});
    sampleData.put("P-20", new double[]{22, 32, 42, 52, 62, 72, 82, 92, 102, 112});

    return sampleData;
  }


}
