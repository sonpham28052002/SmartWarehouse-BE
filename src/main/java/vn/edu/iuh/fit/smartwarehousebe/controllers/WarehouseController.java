package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
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
    private final DeliveryNotePdfService deliveryNotePdfService;
    private final WarehouseReceiptPdfService warehouseReceiptPdfService;

    public WarehouseController(WarehouseService warehouseService, DeliveryNotePdfService deliveryNotePdfService, WarehouseReceiptPdfService warehouseReceiptPdfService) {
        this.warehouseService = warehouseService;
        this.deliveryNotePdfService = deliveryNotePdfService;
        this.warehouseReceiptPdfService = warehouseReceiptPdfService;
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseResponse>> getWarehouse(@RequestParam(defaultValue = "1") int current_page, @RequestParam(defaultValue = "10") int per_page, @RequestParam(defaultValue = "id") String sortBy, GetWarehouseQuest request) {

        Page<Warehouse> warehouses = warehouseService.getAll(current_page - 1, per_page, sortBy, request);
        Pageable pageable = PageRequest.of(current_page - 1, per_page, Sort.by(sortBy));

        List<WarehouseResponse> warehouseResponses = warehouses.getContent().stream().map(warehouse -> WarehouseMapper.INSTANCE.toDto(warehouse)).collect(Collectors.toList());

        Page<WarehouseResponse> warehouseResponsePage = new PageImpl<>(warehouseResponses, pageable, warehouses.getTotalElements());

        return ResponseEntity.ok(warehouseResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long id) {
        System.out.println(warehouseService.getById(id));
        return ResponseEntity.ok(WarehouseMapper.INSTANCE.toDto(warehouseService.getById(id)));
    }

    @PostMapping("/create")
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody CreateWarehouseRequest request) {
        Warehouse newWarehouse = Warehouse.builder()
                .code(request.getCode())
                .address(request.getAddress())
                .name(request.getName())
                .manager(User.builder().id(request.getManagerId()).build())
                .rowNum(request.getRowNum())
                .shelfNum(request.getShelfNum())
                .columnNum(request.getColumnNum())
                .staffs(request.getStaffIDs().stream().map(staffId -> User.builder().id(staffId).build()).collect(Collectors.toSet()))
                .build();
        return new ResponseEntity<>(WarehouseMapper.INSTANCE.toDto(warehouseService.create(newWarehouse)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody UpdateWarehouseRequest request) {
        Warehouse updateWarehouse = Warehouse.builder()
                .address(request.getAddress())
                .name(request.getName())
                .code(request.getCode())
                .rowNum(request.getRowNum())
                .shelfNum(request.getShelfNum())
                .columnNum(request.getColumnNum())
                .manager(User.builder().id(request.getManagerId()).build())
                .staffs(request.getStaffIDs().stream().map(staffId -> User.builder().id(staffId).build()).collect(Collectors.toSet()))
                .build();
        return ResponseEntity.ok(WarehouseMapper.INSTANCE.toDto(warehouseService.update(id, updateWarehouse)));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Boolean> deleteWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.delete(id));
    }

    @GetMapping("/{code}/checkCode")
    public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
        return ResponseEntity.ok(warehouseService.checkCodeIsExist(Warehouse.class, code));
    }

    @PostMapping("/export-warehouse-receipt")
    public ResponseEntity<byte[]> exportPdf(@RequestBody @Valid WarehouseReceiptRequest request) {
        byte[] pdfContent = warehouseReceiptPdfService.generatePdf(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "phieu-nhap-kho.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }

    @PostMapping("/export-delivery-note")
    public ResponseEntity<byte[]> exportPdf(@RequestBody @Valid DeliveryNoteRequest request) {
        byte[] pdfContent = deliveryNotePdfService.generatePdf(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "phieu-xuat-kho.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
