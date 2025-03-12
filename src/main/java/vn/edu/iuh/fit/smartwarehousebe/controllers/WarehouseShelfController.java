package vn.edu.iuh.fit.smartwarehousebe.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.WarehouseShelf.WarehouseShelfRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.WarehouseShelf.WarehouseShelfResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.WarehouseShelfService;

@RestController
@RequestMapping("/warehouse_shelf")
public class WarehouseShelfController {

  @Autowired
  private WarehouseShelfService warehouseShelfService;


  @GetMapping("/{warehouseId}")
  public List<WarehouseShelfResponse> getAllShelfIdWareHouseID(@PathVariable Long warehouseId) {
    return warehouseShelfService.getAllShelfIdWareHouseID(warehouseId);
  }

  @GetMapping("/detail/{shelfId}")
  public WarehouseShelfResponse getShelfById(@PathVariable Long shelfId) {
    return warehouseShelfService.getAllShelfByID(shelfId);
  }

  @PostMapping("/{wareHouseID}/create")
  public ResponseEntity<List<WarehouseShelfResponse>> createAndUpdate(
      @PathVariable Long wareHouseID,
      @RequestBody List<WarehouseShelfRequest> warehouseShelfRequests) {
    return ResponseEntity.ok(
        warehouseShelfService.createAndUpdate(wareHouseID, warehouseShelfRequests));
  }
}
