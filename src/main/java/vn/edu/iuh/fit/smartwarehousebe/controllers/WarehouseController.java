package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.servies.DeliveryNotePdfService;
import vn.edu.iuh.fit.smartwarehousebe.servies.WarehouseReceiptPdfService;
import vn.edu.iuh.fit.smartwarehousebe.servies.WarehouseService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

  private final WarehouseService warehouseService;
  private final WarehouseReceiptPdfService warehouseReceiptPdfService;
  private final DeliveryNotePdfService deliveryNotePdfService;

  public WarehouseController(WarehouseService warehouseService, WarehouseReceiptPdfService warehouseReceiptPdfService, DeliveryNotePdfService deliveryNotePdfService) {
    this.warehouseService = warehouseService;
    this.warehouseReceiptPdfService = warehouseReceiptPdfService;
    this.deliveryNotePdfService = deliveryNotePdfService;
  }

  @GetMapping
  public ResponseEntity<Page<WarehouseResponse>> getWarehouse(
      @RequestParam(defaultValue = "1") int current_page,
      @RequestParam(defaultValue = "10") int per_page,
      @RequestParam(defaultValue = "id") String sortBy, GetWarehouseQuest request) {

    return ResponseEntity.ok(warehouseService.getAll(current_page - 1, per_page, sortBy,
        request));
  }

  @GetMapping("/all")
  public ResponseEntity<List<WarehouseResponse>> getWarehouse(GetWarehouseQuest request) {

    List<WarehouseResponse> warehouses = warehouseService.getAll(request);

    return ResponseEntity.ok(warehouses);
  }

  @GetMapping("/{id}")
  public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
    System.out.println(warehouseService.getById(id));
    return ResponseEntity.ok(warehouseService.getById(id));
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
    return new ResponseEntity<>(warehouseService.create(newWarehouse), HttpStatus.CREATED);
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
        warehouseService.update(id, updateWarehouse));
  }

  @DeleteMapping("/{id}/delete")
  public ResponseEntity<Boolean> deleteWarehouse(@PathVariable Long id) {
    return ResponseEntity.ok(warehouseService.delete(id));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(warehouseService.checkCodeIsExist(Warehouse.class, code));
  }

//
//  @PostMapping("/export-delivery-note")
//  public ResponseEntity<byte[]> exportPdf(@RequestBody @Valid DeliveryNoteRequest request) {
//    byte[] pdfContent = deliveryNotePdfService.generatePdf(request);
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_PDF);
//    headers.setContentDispositionFormData("attachment", "phieu-xuat-kho.pdf");
//    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//
//    return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
//  }

}
