package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.GetProductQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.servies.ProductService;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping()
  public ResponseEntity<Page<ProductResponse>> getPage(
      @RequestParam(defaultValue = "1") int current_page,
      @RequestParam(defaultValue = "10") int per_page,
      @RequestParam(defaultValue = "id") String sortBy,
      GetProductQuest productQuest
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(current_page - 1, per_page, sort);
    return ResponseEntity.ok(productService.getAll(pageRequest, productQuest));
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProductResponse>> getAll(GetProductQuest productQuest) {
    return ResponseEntity.ok(productService.getAll(productQuest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getById(id));
  }

  @PostMapping()
  public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
    return new ResponseEntity<>(productService.create(request), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> update(@PathVariable Long id,
      @RequestBody @Valid CreateProductRequest request) {
    System.out.println(request);
    return ResponseEntity.ok(productService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(@PathVariable Long id) {
    return ResponseEntity.ok(productService.delete(id));
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(productService.checkCodeIsExist(Product.class, code));
  }

  @GetMapping("/{warehouseId}/findAllByWarehouseId")
  public ResponseEntity<List<ProductResponse>> findAllByWarehouseId(
      @PathVariable Long warehouseId) {
    return ResponseEntity.ok(productService.findAllByWarehouseId(warehouseId));
  }

  @GetMapping("/{warehouseId}/{partnerId}/findAllByWarehouseIdAndPartnerId")
  public ResponseEntity<List<ProductResponse>> findAllByWarehouseIdAndPartnerId(
      @PathVariable Long warehouseId, @PathVariable Long partnerId) {
    return ResponseEntity.ok(
        productService.findAllByWarehouseIdAndPartnerId(warehouseId, partnerId));
  }
}
