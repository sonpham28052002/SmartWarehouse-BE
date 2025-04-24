package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.repositories.DamagedProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StockTakeRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;

@Service
public class DamagedProductService {

  @Autowired
  private StockTakeRepository stockTakeRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private DamagedProductRepository damagedProductRepository;

  @Transactional
  public Set<DamagedProductResponse> updateAndCreateByStockTakeId(Long stockTakeId,
      Set<DamagedProductResponse> newDamagedProducts) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stocktake not found"));
    Set<DamagedProductResponse> oldProductResponse = damagedProductRepository.findByStockTakeId(
            stockTakeId).stream().map((i) -> DamagedProductMapper.INSTANCE.toDto(i))
        .collect(Collectors.toSet());
    Set<DamagedProductResponse> allDamagedProductResponses = new HashSet<>(oldProductResponse);
    allDamagedProductResponses.addAll(newDamagedProducts);
    Set<DamagedProductResponse> result = new HashSet<>();
    for (DamagedProductResponse response : allDamagedProductResponses) {
      if (response.getId() == null) {
        DamagedProduct damagedProduct = damagedProductRepository.save(DamagedProduct
            .builder()
            .stockTake(stockTake)
            .description(response.getDescription())
            .inventory(Inventory.builder().id(response.getInventory().getId()).build())
            .quantity(response.getQuantity())
            .status(DamagedProductStatus.INACTIVE)
            .build());
        System.out.println(damagedProduct);
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        DamagedProduct damagedProduct = damagedProductRepository.save(DamagedProduct
            .builder()
            .id(response.getId())
            .stockTake(stockTake)
            .description(response.getDescription())
            .inventory(Inventory.builder().id(response.getInventory().getId()).build())
            .quantity(response.getQuantity())
            .status(DamagedProductStatus.INACTIVE)
            .build());
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (!newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        damagedProductRepository.deleteById(response.getId());
      }
    }
    return result;
  }

  @Transactional
  public Set<DamagedProductResponse> updateAndCreateByTransactionId(Long transactionId,
      Set<DamagedProductResponse> newDamagedProducts) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("transaction not found"));
    Set<DamagedProductResponse> oldProductResponse = damagedProductRepository.findByTransactionId(
            transactionId).stream().map((i) -> DamagedProductMapper.INSTANCE.toDto(i))
        .collect(Collectors.toSet());

    Set<DamagedProductResponse> allDamagedProductResponses = new HashSet<>(oldProductResponse);
    allDamagedProductResponses.addAll(newDamagedProducts);
    Set<DamagedProductResponse> result = new HashSet<>();
    for (DamagedProductResponse response : allDamagedProductResponses) {
      if (response.getId() == null) {
        DamagedProduct damagedProduct = damagedProductRepository.save(DamagedProduct
            .builder()
            .transaction(transaction)
            .description(response.getDescription())
            .inventory(Inventory.builder().id(response.getInventory().getId()).build())
            .quantity(response.getQuantity())
            .status(DamagedProductStatus.INACTIVE)
            .build());
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        DamagedProduct damagedProduct = damagedProductRepository.save(DamagedProduct
            .builder()
            .id(response.getId())
            .transaction(transaction)
            .description(response.getDescription())
            .inventory(Inventory.builder().id(response.getInventory().getId()).build())
            .quantity(response.getQuantity())
            .status(DamagedProductStatus.INACTIVE)
            .build());
        result.add(DamagedProductMapper.INSTANCE.toDto(damagedProduct));
      } else if (!newDamagedProducts.contains(response) && oldProductResponse.contains(response)) {
        damagedProductRepository.deleteById(response.getId());
      }
    }
    return result;
  }
}
