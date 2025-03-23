package vn.edu.iuh.fit.smartwarehousebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.CreateStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.GetUnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.StockTakeService;

@RestController
@RequestMapping("/stock_take")
public class StockTakeController {

  @Autowired
  private StockTakeService stockTakeService;

  @GetMapping
  public ResponseEntity<Page<StockTakeResponse>> getAll(
      @RequestParam(value = "per_page", defaultValue = "10") int perPage,
      @RequestParam(value = "current_page", defaultValue = "1") int currentPage,
      GetStockTakeRequest request) {
    return ResponseEntity.ok(stockTakeService.getAll(
        PageRequest.of(currentPage - 1, perPage, Sort.by(Sort.Direction.DESC, "id")), request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<StockTakeResponse> getStockTakeById(
      @PathVariable Long id) {
    return ResponseEntity.ok(stockTakeService.getStockTakeById(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deleteStockTakeById(
      @PathVariable Long id) {
    return ResponseEntity.ok(stockTakeService.deleteStockTakeById(id));
  }

  @PostMapping("/create")
  public ResponseEntity<StockTakeResponse> createStockTakeById(
      @RequestBody CreateStockTakeRequest request) {
    return ResponseEntity.ok(stockTakeService.createStockTake(request));
  }
}
