package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.GetSupplierQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;
import vn.edu.iuh.fit.smartwarehousebe.servies.SupplierService;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@RestController
@RequestMapping("/suppliers")
public class SupplierController {

  private final SupplierService supplierService;

  public SupplierController(SupplierService supplierService) {
    this.supplierService = supplierService;
  }

  @GetMapping()
  public ResponseEntity<Page<SupplierResponse>> getPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetSupplierQuest supplierQuest
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(supplierService.getAll(pageRequest, supplierQuest));
  }

  @GetMapping("/all")
  public ResponseEntity<List<SupplierResponse>> getAll(GetSupplierQuest supplierQuest) {
    return ResponseEntity.ok(supplierService.getAll(supplierQuest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SupplierResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(supplierService.getById(id));
  }

  @PostMapping()
  public ResponseEntity<SupplierResponse> create(
      @RequestBody @Valid CreateSupplierRequest request) {
    return ResponseEntity.ok(supplierService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SupplierResponse> update(@PathVariable Long id,
      @RequestBody @Valid CreateSupplierRequest request) {
    return ResponseEntity.ok(supplierService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(@PathVariable Long id) {
    return ResponseEntity.ok(supplierService.delete(id));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(supplierService.checkCodeIsExist(Supplier.class, code));
  }
}
