package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.ArimaKMeansService;

@RestController
@RequestMapping()
public class ArimaKMeansController {

  private final ArimaKMeansService arimaKMeansService;

  public ArimaKMeansController(ArimaKMeansService arimaKMeansService) {
    this.arimaKMeansService = arimaKMeansService;
  }

  @PostMapping("/forecast")
  public Map<String, Object> forecast(
      @RequestBody(required = false) List<String> selectedProducts) {
    return arimaKMeansService.forecastSales(selectedProducts);
  }

  @PostMapping("/kmeans")
  public Map<Integer, Map<String, Object>> kMeansProduct(
      @RequestBody(required = false) Map<String, Long> selectedProducts) {
    return arimaKMeansService.clusterProducts(selectedProducts);
  }

}
