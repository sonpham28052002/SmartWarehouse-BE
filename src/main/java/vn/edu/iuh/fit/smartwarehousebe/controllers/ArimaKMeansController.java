package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.*;

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
      @PathVariable String warehouseCode,
      @RequestBody(required = false) List<String> selectedProducts) {

    return arimaKMeansService.forecastSales(selectedProducts, warehouseCode);

  }

  @PostMapping("/forecastv2/{warehouseCode}")
  public Map<String, Object> forecastV2(
      @PathVariable String warehouseCode,
      @RequestBody(required = false) List<String> selectedProducts) throws Exception {
    return arimaKMeansService.forecast(selectedProducts, warehouseCode);
  }

  @PostMapping("/forecastv2")
  public Map<String, Object> forecastV2NoWarehouseCode(
      @RequestBody(required = false) List<String> selectedProducts) throws Exception {
    return arimaKMeansService.forecast(selectedProducts, null);
  }

  @PostMapping("/kmeans")
  public Map<Integer, Map<String, Object>> kMeansProduct(
      @RequestBody(required = false) Map<String, Long> selectedProducts) {
    return arimaKMeansService.clusterProducts(selectedProducts);
  }

}
