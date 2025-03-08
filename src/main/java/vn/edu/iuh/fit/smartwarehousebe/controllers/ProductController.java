package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.GetProductQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.servies.ProductService;

import java.util.List;

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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            GetProductQuest productQuest
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
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
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody @Valid CreateProductRequest request) {
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
}
