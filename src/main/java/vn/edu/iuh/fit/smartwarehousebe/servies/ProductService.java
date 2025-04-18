package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.ProductNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.PartnerMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.ConversionUnit;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.ProductSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

/**
 * @description
 * @author: vie
 * @date: 2/3/25
 */
@Service
public class ProductService extends CommonService<Product> {

  private final ProductRepository productRepository;
  private final PartnerService partnerService;
  private final PartnerMapper partnerMapper;

  private final UnitService unitService;

  private final ConversionUnitService conversionUnitService;
  private final ProductMapper productMapper;

  public ProductService(ProductRepository productRepository, PartnerService partnerService,
      PartnerMapper partnerMapper, UnitService unitService,
      ConversionUnitService conversionUnitService, ProductMapper productMapper) {
    super();
    this.productRepository = productRepository;
    this.partnerService = partnerService;
    this.partnerMapper = partnerMapper;
    this.unitService = unitService;
    this.conversionUnitService = conversionUnitService;
    this.productMapper = productMapper;
  }

  @Cacheable(value = "products", key = "#productQuest + '_' + #pageRequest.pageNumber + '_' + #pageRequest.pageSize")
  public Page<ProductResponse> getAll(PageRequest pageRequest, GetProductQuest productQuest) {
    Specification<Product> specification = SpecificationBuilder.<Product>builder()
        .with(ProductSpecification.hasCode(productQuest.getCode()))
        .with(ProductSpecification.hasSku(productQuest.getSku()))
        .with(ProductSpecification.hasName(productQuest.getName()))
        .with(ProductSpecification.hasPartnerId(productQuest.getPartnerId())).build();

    return productRepository.findAll(specification, pageRequest)
        .map((i) -> ProductMapper.INSTANCE.toDto(i));
  }

  @Cacheable(value = "products", key = "#productQuest")
  public List<ProductResponse> getAll(GetProductQuest productQuest) {
    Specification<Product> specification = SpecificationBuilder.<Product>builder()
        .with(ProductSpecification.hasActive(productQuest.getActive()))
        .with(ProductSpecification.hasCode(productQuest.getCode()))
        .with(ProductSpecification.hasSku(productQuest.getSku()))
        .with(ProductSpecification.hasName(productQuest.getName()))
        .with(ProductSpecification.hasPartnerId(productQuest.getPartnerId())).build();

    return productRepository.findAll(specification).stream()
        .map((i) -> ProductMapper.INSTANCE.toDto(i)).toList();
  }

  @CacheEvict(value = "products", allEntries = true)
  public ProductResponse getById(Long id) {
    return productRepository.findById(id).map((i) -> ProductMapper.INSTANCE.toDto(i))
        .orElseThrow(ProductNotFoundException::new);
  }

  @Transactional
  @CacheEvict(value = "products", allEntries = true)
  public ProductResponse create(CreateProductRequest productRequest) {

    Product product = ProductMapper.INSTANCE.toEntity(productRequest);
    Unit unit = UnitMapper.INSTANCE.toEntity(unitService.getUnitById(productRequest.getUnitId()));
    PartnerResponse partner = partnerService.getById(productRequest.getPartnerId());
    product.setPartner(partnerMapper.toEntity(partner));
    product.setUnit(unit);

    Product newProduct = productRepository.save(product);
    List<ConversionUnit> conversionUnits = new ArrayList<>();
    if (productRequest.getConversionUnits() != null) {
      productRequest.getConversionUnits().forEach(conversionUnit -> {
        conversionUnits.add(
            conversionUnitService.create(unit.getId(), conversionUnit.getFromUnit().getId(),
                newProduct.getId(), conversionUnit.getConversionRate()));
      });
    }
    newProduct.setConversionUnits(conversionUnits);

    return ProductMapper.INSTANCE.toDto(productRepository.save(newProduct));
  }

  @Transactional
  @CacheEvict(value = "products", allEntries = true)
  public ProductResponse update(Long id, CreateProductRequest productRequest) {
    Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    for (ConversionUnit conversionUnit : product.getConversionUnits()) {
      conversionUnitService.delete(conversionUnit.getId());
    }

    ProductMapper.INSTANCE.partialUpdate(productRequest, product);

    if (product.getUnit().getId() != productRequest.getUnitId()) {
      Unit unit = UnitMapper.INSTANCE.toEntity(unitService.getUnitById(productRequest.getUnitId()));
      product.setUnit(unit);
    }

    if (product.getPartner().getId() != productRequest.getPartnerId()) {
      PartnerResponse partner = partnerService.getById(productRequest.getPartnerId());
      product.setPartner(partnerMapper.toEntity(partner));
    }

    Product newProduct = productRepository.save(product);

    List<ConversionUnit> conversionUnits = new ArrayList<>();
    if (productRequest.getConversionUnits() != null) {
      productRequest.getConversionUnits().forEach(conversionUnit -> {
        conversionUnits.add(conversionUnitService.create(newProduct.getUnit().getId(),
            conversionUnit.getFromUnit().getId(), newProduct.getId(),
            conversionUnit.getConversionRate()));
      });
    }
    newProduct.setConversionUnits(conversionUnits);
    return ProductMapper.INSTANCE.toDto(productRepository.save(newProduct));
  }

  @Transactional
  @CacheEvict(value = "products", allEntries = true)
  public boolean delete(Long id) {
    return productRepository.findById(id).map(product -> {
      productRepository.delete(product);
      return true;
    }).orElseThrow(ProductNotFoundException::new);
  }

  @Transactional
  @CacheEvict(value = "products", allEntries = true)
  public List<ProductResponse> findAllByWarehouseId(Long warehouseId) {
    return productRepository.findAllByWarehouseId(warehouseId).stream()
        .map((i) -> ProductMapper.INSTANCE.toDto(i)).collect(
            Collectors.toList());
  }

  @Transactional
  @CacheEvict(value = "products", allEntries = true)
  public List<ProductResponse> findAllByWarehouseIdAndPartnerId(Long warehouseId, Long partnerId) {
    return productRepository.findAllByWarehouseIdAAndPartnerId(warehouseId, partnerId).stream()
        .map((i) -> ProductMapper.INSTANCE.toDto(i)).collect(
            Collectors.toList());
  }

  public List<ProductResponse> getByIds(List<Long> productIds) {
    return productRepository.findByIdIn(productIds).stream()
        .map(productMapper::toDto).toList();
  }

  public ProductResponse getByCode(String productCode) {
    return productRepository.findByCode(productCode).map(productMapper::toDto)
        .orElseThrow(ProductNotFoundException::new);
  }
}
