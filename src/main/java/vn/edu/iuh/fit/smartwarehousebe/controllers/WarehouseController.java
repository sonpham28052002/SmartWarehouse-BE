package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.servies.WarehouseService;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

  @Autowired
  private WarehouseService warehouseService;

  @GetMapping
  public ResponseEntity<Page<WarehouseResponse>> getWarehouse(
      @RequestParam(defaultValue = "1") int current_page,
      @RequestParam(defaultValue = "10") int per_page,
      @RequestParam(defaultValue = "id") String sortBy, GetWarehouseQuest request) {

    Page<WarehouseResponse> warehouses = warehouseService.getAll(current_page - 1, per_page, sortBy,
        request).map(i -> WarehouseMapper.INSTANCE.toDto(i));

    return ResponseEntity.ok(warehouses);
  }

  @GetMapping("/all")
  public ResponseEntity<List<WarehouseResponse>> getWarehouse(GetWarehouseQuest request) {

    List<WarehouseResponse> warehouses = warehouseService.getAll(request)
        .stream()
        .map(i -> WarehouseMapper.INSTANCE.toDto(i)).collect(Collectors.toList());

    return ResponseEntity.ok(warehouses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
    System.out.println(warehouseService.getById(id));
    return ResponseEntity.ok(WarehouseMapper.INSTANCE.toDto(warehouseService.getById(id)));
  }

  @PostMapping("/create")
  public ResponseEntity<WarehouseResponse> createWarehouse(
      @Valid @RequestBody CreateWarehouseRequest request) {
    Warehouse newWarehouse = Warehouse.builder()
        .code(request.getCode())
        .address(request.getAddress())
        .name(request.getName())
        .manager(User.builder().id(request.getManagerId()).build())
        .staffs(request.getStaffIDs().stream().map(staffId -> User.builder().id(staffId).build())
            .collect(Collectors.toSet()))
        .build();
    return new ResponseEntity<>(
        WarehouseMapper.INSTANCE.toDto(warehouseService.create(newWarehouse)), HttpStatus.CREATED);
  }

  @PutMapping("/{id}/update")
  public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id,
      @Valid @RequestBody UpdateWarehouseRequest request) {
    Warehouse updateWarehouse = Warehouse.builder()
        .address(request.getAddress())
        .name(request.getName())
        .code(request.getCode())
        .manager(User.builder().id(request.getManagerId()).build())
        .staffs(request.getStaffIDs().stream().map(staffId -> User.builder().id(staffId).build())
            .collect(Collectors.toSet()))
        .build();
    return ResponseEntity.ok(
        WarehouseMapper.INSTANCE.toDto(warehouseService.update(id, updateWarehouse)));
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<Boolean> deleteWarehouse(@PathVariable Long id) {
    return ResponseEntity.ok(warehouseService.delete(id));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(warehouseService.checkCodeIsExist(Warehouse.class, code));
  }


}
