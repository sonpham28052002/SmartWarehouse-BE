package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTakeDetail.GetStockTakeDetailRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.StockTakeDetailService;

@RestController
@RequestMapping("/stock_take_detail")
public class StockTakeDetailController {


  @Autowired
  private StockTakeDetailService stockTakeDetailService;

  @GetMapping("{stockTakeId}/all")
  public ResponseEntity<Page<StockTakeDetailResponse>> getAll(@PathVariable Long stockTakeId,
      @RequestParam(value = "per_page", defaultValue = "10") int perPage,
      @RequestParam(value = "current_page", defaultValue = "1") int currentPage,
      GetStockTakeDetailRequest request) {
    return ResponseEntity.ok(stockTakeDetailService.getAll(stockTakeId,
        PageRequest.of(currentPage - 1, perPage, Sort.by(Sort.Direction.DESC, "id")), request));
  }
}
