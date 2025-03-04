package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.GetProductQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.ProductNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.SupplierMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.ProductSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class ProductService {
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

    @Cacheable(value = "products", key = "#productQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
    public Page<ProductResponse> getAll(PageRequest pageRequest, GetProductQuest productQuest) {
        Specification<Product> specification = SpecificationBuilder.<Product>builder()
                .with(ProductSpecification.hasCode(productQuest.getCode()))
                .with(ProductSpecification.hasSku(productQuest.getSku()))
                .with(ProductSpecification.hasName(productQuest.getName()))
                .with(ProductSpecification.hasSupplierId(productQuest.getSupplierId()))
                .build();

        return productRepository.findAll(specification, pageRequest).map(productMapper::toDto);
    }
    @Cacheable(value = "products", key = "#productQuest")
    public List<ProductResponse> getAll(GetProductQuest productQuest) {
        Specification<Product> specification = SpecificationBuilder.<Product>builder()
                .with(ProductSpecification.hasActive(productQuest.getActive()))
                .with(ProductSpecification.hasCode(productQuest.getCode()))
                .with(ProductSpecification.hasSku(productQuest.getSku()))
                .with(ProductSpecification.hasName(productQuest.getName()))
                .with(ProductSpecification.hasSupplierId(productQuest.getSupplierId()))
                .build();

        return productRepository.findAll(specification).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse getById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(ProductNotFoundException::new);
    }
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse create(CreateProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        SupplierResponse supplier = supplierService.getById(productRequest.getSupplierId());
        product.setSupplier(supplierMapper.toEntity(supplier));
        return productMapper.toDto(productRepository.save(product));
    }
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse update(Long id, CreateProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productMapper.partialUpdate(productRequest, product);
        SupplierResponse supplier = supplierService.getById(productRequest.getSupplierId());
        product.setSupplier(supplierMapper.toEntity(supplier));
        return productMapper.toDto(productRepository.save(product));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public boolean delete(Long id) {
        return productRepository
                .findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                })
                .orElseThrow(ProductNotFoundException::new);
    }

}
