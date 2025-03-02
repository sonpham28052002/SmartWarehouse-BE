package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.GetWarehouseQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.UpdateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.WarehouseService;

import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 1/3/25
 */
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
   private final WarehouseService warehouseService;

   public WarehouseController(WarehouseService warehouseService) {
      this.warehouseService = warehouseService;
   }

   @GetMapping
   public ResponseEntity<List<WarehouseResponse>> getWarehouse(
         @RequestParam (defaultValue = "0") int page,
         @RequestParam (defaultValue = "10") int size,
         @RequestParam (defaultValue = "id") String sortBy,
         GetWarehouseQuest request
         ) {
      return ResponseEntity.ok(warehouseService.getAll(page, size, sortBy, request));
   }

   @GetMapping("/{id}")
   public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Integer id) {
      return ResponseEntity.ok(warehouseService.getById(id));
   }

   @PostMapping("/create")
   public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody CreateWarehouseRequest request) {
      return new ResponseEntity<>(warehouseService.create(request), HttpStatus.CREATED);
   }

   @PutMapping("/{id}/update")
   public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Integer id, @Valid @RequestBody UpdateWarehouseRequest request) {
      return ResponseEntity.ok(warehouseService.update(id, request));
   }

   @DeleteMapping("/{id}/delete")
   public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer id) {
      warehouseService.delete(id);
      return ResponseEntity.ok().build();
   }


}
