package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.Ids.StockTakeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.Ids.TransactionDetailId;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.damagedProduct.GetDamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.DamagedProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.DamagedProductSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;

@Service
public class DamagedProductService {

  @Autowired
  private StockTakeRepository stockTakeRepository;

  @Autowired
  private StockTakeDetailRepository stockTakeDetailRepository;


  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private TransactionDetailRepository transactionDetailRepository;

  @Autowired
  private DamagedProductRepository damagedProductRepository;

  @Transactional
  public Set<DamagedProductResponse> updateAndCreateByStockTakeId(Long stockTakeId,
      Long inventoryId, Set<DamagedProductWithResponse> damagedProducts) {

    StockTakeDetailId stockTakeDetailId = StockTakeDetailId.builder()
        .stockTakeId(stockTakeId)
        .inventoryId(inventoryId)
        .build();

    StockTakeDetail stockTakeDetail = stockTakeDetailRepository.findById(stockTakeDetailId)
        .orElseThrow(() -> new EntityNotFoundException("StockTakeDetail not found"));

    Set<DamagedProductResponse> newDamagedProducts = mapToDamagedProductResponses(damagedProducts,
        stockTakeDetail);

    Set<DamagedProductResponse> oldProductResponses = getOldDamagedProducts(stockTakeDetailId);

    Set<DamagedProductResponse> allDamagedProductResponses = new HashSet<>(newDamagedProducts);
    allDamagedProductResponses.addAll(oldProductResponses);

    return handleDamagedProductResponses(allDamagedProductResponses, newDamagedProducts,
        oldProductResponses, stockTakeDetail);
  }

  private Set<DamagedProductResponse> mapToDamagedProductResponses(
      Set<DamagedProductWithResponse> damagedProducts, StockTakeDetail stockTakeDetail) {
    return damagedProducts.stream()
        .map(damagedProduct -> DamagedProductResponse.builder()
            .stockTakeDetail(StockTakeDetailMapper.INSTANCE.toDto(stockTakeDetail))
            .id(damagedProduct.getId())
            .type(damagedProduct.getType())
            .status(damagedProduct.getStatus())
            .description(damagedProduct.getDescription())
            .quantity(damagedProduct.getQuantity())
            .build())
        .collect(Collectors.toSet());
  }

  private Set<DamagedProductResponse> mapToDamagedProductResponsesByTransactionDetail(
      Set<DamagedProductWithResponse> damagedProducts, TransactionDetail transactionDetail) {
    return damagedProducts.stream()
        .map(damagedProduct -> DamagedProductResponse.builder()
            .transactionDetail(TransactionDetailMapper.INSTANCE.toDto(transactionDetail))
            .id(damagedProduct.getId())
            .type(damagedProduct.getType())
            .status(damagedProduct.getStatus())
            .description(damagedProduct.getDescription())
            .quantity(damagedProduct.getQuantity())
            .build())
        .collect(Collectors.toSet());
  }

  private Set<DamagedProductResponse> getOldDamagedProducts(StockTakeDetailId stockTakeDetailId) {
    return damagedProductRepository.findByStockTakeDetailId(stockTakeDetailId).stream()
        .map(DamagedProductMapper.INSTANCE::toDto)
        .collect(Collectors.toSet());
  }

  private Set<DamagedProductResponse> handleDamagedProductResponses(
      Set<DamagedProductResponse> allDamagedProductResponses,
      Set<DamagedProductResponse> newDamagedProducts,
      Set<DamagedProductResponse> oldProductResponses,
      StockTakeDetail stockTakeDetail) {

    Set<DamagedProductResponse> result = new HashSet<>();

    for (DamagedProductResponse response : allDamagedProductResponses) {
      Optional<TransactionDetail> transactionDetailOpt = Optional.ofNullable(
              response.getTransactionDetail())
          .flatMap(td -> transactionDetailRepository.findById(TransactionDetailId.builder()
              .transactionId(td.getTransactionId())
              .inventoryId(td.getInventory().getId())
              .build()));

      if (response.getId() == null) {
        DamagedProduct damagedProduct = createDamagedProduct(response, stockTakeDetail,
            transactionDetailOpt.orElse(null));
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (newDamagedProducts.contains(response) && oldProductResponses.contains(response)) {
        DamagedProduct damagedProduct = updateDamagedProduct(response, stockTakeDetail,
            transactionDetailOpt.orElse(null));
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (!newDamagedProducts.contains(response) && oldProductResponses.contains(response)) {
        deleteDamagedProduct(response.getId());
      }
    }

    return result;
  }

  private DamagedProduct createDamagedProduct(DamagedProductResponse response,
      StockTakeDetail stockTakeDetail, TransactionDetail transactionDetail) {
    return damagedProductRepository.save(DamagedProduct.builder()
        .stockTakeDetail(stockTakeDetail)
        .transactionDetail(transactionDetail)
        .description(response.getDescription())
        .quantity(response.getQuantity())
        .exchangeType(ExchangeType.UNSPECIFIED)
        .status(DamagedProductStatus.INACTIVE)
        .type(response.getType())
        .build());
  }

  private DamagedProduct updateDamagedProduct(DamagedProductResponse response,
      StockTakeDetail stockTakeDetail, TransactionDetail transactionDetail) {
    return damagedProductRepository.save(DamagedProduct.builder()
        .id(response.getId())
        .stockTakeDetail(stockTakeDetail)
        .transactionDetail(transactionDetail)
        .description(response.getDescription())
        .type(response.getType())
        .exchangeType(ExchangeType.UNSPECIFIED)
        .quantity(response.getQuantity())
        .status(DamagedProductStatus.INACTIVE)
        .build());
  }

  private void deleteDamagedProduct(Long id) {
    damagedProductRepository.deleteById(id);
  }

  @Transactional
  public Set<DamagedProductResponse> updateAndCreateByTransactionId(Long transactionId, Long inventoryId,
      Set<DamagedProductWithResponse> damagedProducts) {
    TransactionDetailId transactionDetailId = TransactionDetailId.builder().transactionId(transactionId).inventoryId(inventoryId).build();
    TransactionDetail transactionDetail = transactionDetailRepository.findById(transactionDetailId)
        .orElseThrow(() -> new NotFoundException("transaction detail not found"));
    Set<DamagedProductResponse> newDamagedProducts = mapToDamagedProductResponsesByTransactionDetail(
        damagedProducts, transactionDetail);

    Set<DamagedProductResponse> oldProductResponse = damagedProductRepository.findByTransactionDetailId(
            transactionDetailId).stream().map((i) -> DamagedProductMapper.INSTANCE.toDto(i))
        .collect(Collectors.toSet());

    Set<DamagedProductResponse> allDamagedProductResponses = new HashSet<>(newDamagedProducts);
    allDamagedProductResponses.addAll(oldProductResponse);
    Set<DamagedProductResponse> result = new HashSet<>();
    for (DamagedProductResponse response : allDamagedProductResponses) {
      if (response.getId() == null) {
        DamagedProduct damagedProduct = createDamagedProduct(response, null, transactionDetail);
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        DamagedProduct damagedProduct = updateDamagedProduct(response, null, transactionDetail);
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (!newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        deleteDamagedProduct(response.getId());
      }
    }
    return result;
  }

  public Page<DamagedProductResponse> getAll(PageRequest pageRequest, GetDamagedProduct request) {
    Specification<DamagedProduct> specification = SpecificationBuilder.<DamagedProduct>builder()
        .with(DamagedProductSpecification.hasProductCode(request.getProductCode()))
        .with(DamagedProductSpecification.hasProductName(request.getProductName()))
        .with(DamagedProductSpecification.hasInventoryName(request.getInventoryName()))
        .with(DamagedProductSpecification.hasTransactionProductCode(request.getProductCode()))
        .with(DamagedProductSpecification.hasTransactionProductName(request.getProductName()))
        .with(DamagedProductSpecification.hasTransactionInventoryName(request.getInventoryName()))
        .with(DamagedProductSpecification.hasStockTakeCode(request.getStockTakeCode()))
        .with(DamagedProductSpecification.hasTransactionCode(request.getTransactionCode()))
        .with(DamagedProductSpecification.hasSupplierName(request.getSupplierName()))
        .with(DamagedProductSpecification.hasSupplierCode(request.getSupplierCode()))
        .with(DamagedProductSpecification.hasTransactionSupplierName(request.getSupplierName()))
        .with(DamagedProductSpecification.hasTransactionSupplierCode(request.getSupplierCode()))
        .with(DamagedProductSpecification.hasExchangeStatus(request.getExchangeStatus()))
        .with(DamagedProductSpecification.hasExchangeType(request.getExchangeType()))
        .with(DamagedProductSpecification.hasDeleted(false))
        .build();

    return damagedProductRepository.findAll(specification, pageRequest)
        .map((i) -> DamagedProductMapper.INSTANCE.toDto(i));
  }

  public List<DamagedProductResponse> getAll(GetDamagedProduct request) {
    Specification<DamagedProduct> specification = SpecificationBuilder.<DamagedProduct>builder()
        .with(DamagedProductSpecification.hasProductCode(request.getProductCode()))
        .with(DamagedProductSpecification.hasProductName(request.getProductName()))
        .with(DamagedProductSpecification.hasInventoryName(request.getInventoryName()))
        .with(DamagedProductSpecification.hasTransactionProductCode(request.getProductCode()))
        .with(DamagedProductSpecification.hasTransactionProductName(request.getProductName()))
        .with(DamagedProductSpecification.hasTransactionInventoryName(request.getInventoryName()))
        .with(DamagedProductSpecification.hasStockTakeCode(request.getStockTakeCode()))
        .with(DamagedProductSpecification.hasTransactionCode(request.getTransactionCode()))
        .with(DamagedProductSpecification.hasSupplierName(request.getSupplierName()))
        .with(DamagedProductSpecification.hasSupplierCode(request.getSupplierCode()))
        .with(DamagedProductSpecification.hasTransactionSupplierName(request.getSupplierName()))
        .with(DamagedProductSpecification.hasTransactionSupplierCode(request.getSupplierCode()))
        .with(DamagedProductSpecification.hasExchangeStatus(DamagedProductStatus.NOT_RETURNED.name()))
        .with(DamagedProductSpecification.hasExchangeType(request.getExchangeType()))
        .with(DamagedProductSpecification.hasDeleted(false))
        .build();

    return damagedProductRepository.findAll(specification)
        .stream()
        .map((i) -> DamagedProductMapper.INSTANCE.toDto(i)).collect(Collectors.toList());
  }
}
