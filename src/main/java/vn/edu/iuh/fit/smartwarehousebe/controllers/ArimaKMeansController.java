package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import vn.edu.iuh.fit.smartwarehousebe.servies.ArimaKMeansService;

@RestController
@RequestMapping("/forecast")
public class ArimaKMeansController {

  private final ArimaKMeansService arimaKMeansService;

  public ArimaKMeansController(ArimaKMeansService arimaKMeansService) {
    this.arimaKMeansService = arimaKMeansService;
  }

  @PostMapping("/forecast")
  public Map<String, Object> forecast(
      @RequestBody(required = false) List<String> selectedProducts)  {

    return arimaKMeansService.forecastSales(selectedProducts);

  }

  @PostMapping("/forecastv2")
  public Map<String, Object> forecastV2(
          @RequestBody(required = false) List<String> selectedProducts) throws Exception {
    return arimaKMeansService.forecast(selectedProducts);
  }

  @PostMapping("/kmeans")
  public Map<Integer, Map<String, Object>> kMeansProduct(
      @RequestBody(required = false) Map<String, Long> selectedProducts) {
    return arimaKMeansService.clusterProducts(selectedProducts);
  }

}
