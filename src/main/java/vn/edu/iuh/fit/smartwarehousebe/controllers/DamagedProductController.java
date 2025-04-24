package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.servies.DamagedProductService;

@RestController
@RequestMapping("/damaged_products")
public class DamagedProductController {

  @Autowired
  private DamagedProductService damagedProductService;

  @PostMapping("/api/damaged_products/{stockTakeId}/updateAndCreateByStockTakeId")
  public Set<DamagedProductResponse> updateAndCreateByStockTakeId(
      @PathVariable Long stockTakeId,
      @RequestBody Set<DamagedProductResponse> request) {
    System.out.println(request.size());
    return damagedProductService.updateAndCreateByStockTakeId(stockTakeId, request);
  }


  @PostMapping("{transactionId}/updateAndCreateByTransactionId")
  public Set<DamagedProductResponse> updateAndCreateByTransactionId(
      @PathVariable Long transactionId,
      @RequestBody Set<DamagedProductResponse> request) {
    return damagedProductService.updateAndCreateByTransactionId(transactionId, request);
  }

}
