package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.damagedProduct.GetDamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.GetPartnerQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.servies.DamagedProductService;

@RestController
@RequestMapping("/damaged_products")
public class DamagedProductController {

  @Autowired
  private DamagedProductService damagedProductService;

  @PostMapping("{stockTakeId}/{inventoryId}/updateAndCreateByStockTakeId")
  public Set<DamagedProductResponse> updateAndCreateByStockTakeId(
      @PathVariable Long stockTakeId,
      @PathVariable Long inventoryId,
      @RequestBody Set<DamagedProductWithResponse> request) {
    return damagedProductService.updateAndCreateByStockTakeId(stockTakeId, inventoryId, request);
  }


  @PostMapping("{transactionId}/{inventoryId}/updateAndCreateByTransactionId")
  public Set<DamagedProductResponse> updateAndCreateByTransactionId(
      @PathVariable Long transactionId,
      @PathVariable Long inventoryId,
      @RequestBody Set<DamagedProductWithResponse> request) {
    return damagedProductService.updateAndCreateByTransactionId(transactionId, inventoryId, request);
  }

  @GetMapping()
  public ResponseEntity<Page<DamagedProductResponse>> getPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetDamagedProduct request
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(damagedProductService.getAll(pageRequest, request));
  }

}
