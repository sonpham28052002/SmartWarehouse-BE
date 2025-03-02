package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.SupplierMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;

import java.util.NoSuchElementException;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class ProductService extends SoftDeleteService<Product> {
   private final ProductRepository productRepository;
   private final ProductMapper productMapper;
   private final SupplierService supplierService;
   private final SupplierMapper supplierMapper;

   public ProductService(ProductRepository productRepository,
                         ProductMapper productMapper, SupplierService supplierService, SupplierMapper supplierMapper) {
      super();
      this.productRepository = productRepository;
      this.productMapper = productMapper;
      this.supplierService = supplierService;
      this.supplierMapper = supplierMapper;
   }

   public Page<ProductResponse> getAll(PageRequest pageRequest, GetUserQuest userQuest) {
      Specification<Product> specification = Specification.where(null);
      // TODO: add filter
      return productRepository.findAll(pageRequest).map(productMapper::toDto);
   }

   public ProductResponse getById(Long id) {
      return productRepository.findById(id).map(productMapper::toDto)
            .orElseThrow(() -> new NoSuchElementException("Product not found"));
   }

   public ProductResponse create(CreateProductRequest productRequest) {
      Product product = productMapper.toEntity(productRequest);
      SupplierResponse supplier = supplierService.getById(productRequest.getSupplierId());
      product.setSupplier(supplierMapper.toEntity(supplier));
      return productMapper.toDto(productRepository.save(product));
   }

}
