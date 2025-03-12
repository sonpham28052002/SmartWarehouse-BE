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
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.ConversionUnit;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.ProductSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class ProductService extends CommonService<Product> {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    private final UnitService unitService;

    private final ConversionUnitService conversionUnitService;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper, SupplierService supplierService, SupplierMapper supplierMapper, UnitService unitService, ConversionUnitService conversionUnitService) {
        super();
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.supplierService = supplierService;
        this.supplierMapper = supplierMapper;
        this.unitService = unitService;
        this.conversionUnitService = conversionUnitService;
    }

    @Cacheable(value = "products", key = "#productQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
    public Page<ProductResponse> getAll(PageRequest pageRequest, GetProductQuest productQuest) {
        Specification<Product> specification = SpecificationBuilder.<Product>builder().with(ProductSpecification.hasCode(productQuest.getCode())).with(ProductSpecification.hasSku(productQuest.getSku())).with(ProductSpecification.hasName(productQuest.getName())).with(ProductSpecification.hasSupplierId(productQuest.getSupplierId())).build();

        return productRepository.findAll(specification, pageRequest).map(productMapper::toDto);
    }

    @Cacheable(value = "products", key = "#productQuest")
    public List<ProductResponse> getAll(GetProductQuest productQuest) {
        Specification<Product> specification = SpecificationBuilder.<Product>builder().with(ProductSpecification.hasActive(productQuest.getActive())).with(ProductSpecification.hasCode(productQuest.getCode())).with(ProductSpecification.hasSku(productQuest.getSku())).with(ProductSpecification.hasName(productQuest.getName())).with(ProductSpecification.hasSupplierId(productQuest.getSupplierId())).build();

        return productRepository.findAll(specification).stream().map(productMapper::toDto).toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse getById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto).orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse create(CreateProductRequest productRequest) {

        Product product = productMapper.toEntity(productRequest);
        Unit unit = UnitMapper.INSTANCE.toEntity(unitService.getUnitById(productRequest.getUnitId()));
        SupplierResponse supplier = supplierService.getById(productRequest.getSupplierId());
        product.setSupplier(supplierMapper.toEntity(supplier));
        product.setUnit(unit);

        Product newProduct = productRepository.save(product);
        List<ConversionUnit> conversionUnits = new ArrayList<>();
        if (productRequest.getConversionUnits() != null) {
            productRequest.getConversionUnits().forEach(conversionUnit -> {
                conversionUnits.add(conversionUnitService.create(unit.getId(), conversionUnit.getFromUnit().getId(), newProduct.getId(), conversionUnit.getConversionRate()));
            });
        }
        newProduct.setConversionUnits(conversionUnits);

        return productMapper.toDto(productRepository.save(newProduct));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse update(Long id, CreateProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        for (ConversionUnit conversionUnit: product.getConversionUnits()) {
            conversionUnitService.delete(conversionUnit.getId());
        }

        productMapper.partialUpdate(productRequest, product);

        if (product.getUnit().getId() != productRequest.getUnitId()) {
            Unit unit = UnitMapper.INSTANCE.toEntity(unitService.getUnitById(productRequest.getUnitId()));
            product.setUnit(unit);
        }

        if (product.getSupplier().getId() != productRequest.getSupplierId()) {
            SupplierResponse supplier = supplierService.getById(productRequest.getSupplierId());
            product.setSupplier(supplierMapper.toEntity(supplier));
        }

        Product newProduct = productRepository.save(product);

        List<ConversionUnit> conversionUnits = new ArrayList<>();
        if (productRequest.getConversionUnits() != null) {
            productRequest.getConversionUnits().forEach(conversionUnit -> {
                conversionUnits.add(conversionUnitService.create(newProduct.getUnit().getId(), conversionUnit.getFromUnit().getId(), newProduct.getId(), conversionUnit.getConversionRate()));
            });
        }
        newProduct.setConversionUnits(conversionUnits);
        return productMapper.toDto(productRepository.save(newProduct));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public boolean delete(Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElseThrow(ProductNotFoundException::new);
    }

}
