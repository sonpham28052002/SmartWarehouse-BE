package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.fit.smartwarehousebe.Ids.StockTakeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.CreateStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake.GetStockTakeRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.*;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.StockTakeMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.*;
import vn.edu.iuh.fit.smartwarehousebe.repositories.*;
import vn.edu.iuh.fit.smartwarehousebe.specifications.StockTakeSpecification;

@Service
public class StockTakeService {

  @Autowired
  @Lazy
  private StockTakeRepository stockTakeRepository;

  @Autowired
  @Lazy
  private StockTakeDetailRepository stockTakeDetailRepository;

  @Autowired
  @Lazy
  private InventoryRepository inventoryRepository;


  @Autowired
  @Lazy
  private WarehouseRepository warehouseRepository;
  @Autowired
  private TransactionDetailRepository transactionDetailRepository;
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private TransactionService transactionService;

  @Autowired
  private DamagedProductService damagedProductService;

  @Autowired
  private DamagedProductRepository damagedProductRepository;

  @Transactional(readOnly = true)
  public Page<StockTakeResponse> getAll(PageRequest pageRequest, GetStockTakeRequest request) {
    Specification<StockTake> specification = Specification.where(null);
    if (request.getWarehouseCode() != null) {
      specification = specification.and(
          StockTakeSpecification.hasWarehouseCode(request.getWarehouseCode()));
    }

    if (request.getCode() != null) {
      specification = specification.and(StockTakeSpecification.hasCode(request.getCode()));
    }

    if (request.getStatus() != null) {
      specification = specification.and(
          StockTakeSpecification.hasStatus(request.getStatus().getStatus()));
    }
    return stockTakeRepository.findAll(specification, pageRequest)
        .map(StockTakeMapper.INSTANCE::toDto);
  }

  @Transactional(readOnly = true)
  public List<StockTakeResponse> getAll(GetStockTakeRequest request) {
    Specification<StockTake> specification = Specification.where(null);
    if (request.getWarehouseCode() != null) {
      specification = specification.and(StockTakeSpecification.hasCode(request.getWarehouseCode()));
    }

    if (request.getStatus() != null) {
      specification = specification.and(
          StockTakeSpecification.hasStatus(request.getStatus().getStatus()));
    }

    if (request.getCode() != null) {
      specification = specification.and(StockTakeSpecification.hasCode(request.getCode()));
    }

    if (request.getEndDate() != null && request.getStartDate() != null) {
      specification = specification.and(
          StockTakeSpecification.hasStockTakeDateBetween(request.getStartDate(),
              request.getEndDate()));
    }

    return stockTakeRepository.findAll(specification)
        .stream()
        .map(StockTakeMapper.INSTANCE::toDto).toList();
  }

  @Transactional
  public StockTakeResponse getStockTakeById(Long id) {
    return StockTakeMapper.INSTANCE.toDto(stockTakeRepository.findById(id)
        .orElseThrow(null));
  }

  @Transactional
  public boolean deleteStockTakeById(Long id) {
    try {
      StockTake stockTake = stockTakeRepository.findById(id)
          .orElseThrow(() -> new NotFoundException("stock take not fond"));
      for (StockTakeDetail detail : stockTake.getStockTakeDetails()) {
        stockTakeDetailRepository.delete(detail);
      }
      stockTakeRepository.deleteById(id);
      return true;
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return false;
  }


  @Transactional
  public StockTakeResponse createStockTake(CreateStockTakeRequest request, User user) {
    StockTake stockTake = stockTakeRepository.save(
        StockTake.builder().description(request.getDescription()).warehouse(
                warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new NotFoundException("warehouse not fond")))
            .creator(user)
            .code(generateStockTakeCode())
            .status(StockTakeStatus.PENDING).build());

    List<Inventory> inventories = new ArrayList<>();

    if (request.getProductIds() != null) {
      inventories = inventoryRepository.findByProductIdIn(
          request.getProductIds());
    } else {
      inventories = inventoryRepository.findByStorageLocationWarehouseShelfIdIn(
          request.getShelfIds());
    }

    List<StockTakeDetail> stockTakeDetails = new ArrayList<>();

    for (Inventory inventory : inventories) {
      if (inventory.getStatus() == InventoryStatus.ACTIVE) {
        stockTakeDetails.add(stockTakeDetailRepository.save(
            StockTakeDetail.builder()
                .id(StockTakeDetailId.builder().stockTakeId(stockTake.getId())
                    .inventoryId(inventory.getId()).build())
                .stockTake(stockTake).status(StockTakeDetailStatus.UNVERIFIED)
                .actualQuantity(0L).expectedQuantity(0L).damagedQuantity(0L).inventory(inventory)
                .description(null).build()));
      }
    }
    stockTake.setStockTakeDetails(stockTakeDetails);
    return StockTakeMapper.INSTANCE.toDto(stockTake);
  }

  @Transactional
  public StockTakeResponse startStockTake(Long stockTakeId, User user) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stoke take not found"));
    stockTake.setStatus(StockTakeStatus.IN_PROGRESS);
    stockTake.setExecutor(user);
    StockTake newStockTake = stockTakeRepository.save(stockTake);
    List<StockTakeDetail> stockTakeDetails = new ArrayList<>();
    for (StockTakeDetail detail : stockTake.getStockTakeDetails()) {
      if (detail.getStatus() == StockTakeDetailStatus.UNVERIFIED) {
        detail.setExpectedQuantity(detail.getInventory().getQuantity());
        stockTakeDetails.add(stockTakeDetailRepository.save(detail));
      } else {
        stockTakeDetails.add(detail);
      }
    }
    newStockTake.setStockTakeDetails(stockTakeDetails);
    List<StockTakeDetailResponse> stockTakeDetailResponses = stockTakeDetails.stream()
        .map((i) -> StockTakeDetailMapper.INSTANCE.toDto(i)).collect(
            Collectors.toList());
    StockTakeResponse response = StockTakeMapper.INSTANCE.toDto(newStockTake);
    response.setStockTakeDetails(stockTakeDetailResponses);
    return response;
  }

  @Transactional
  public StockTakeResponse saveStockTake(Long stockTakeId, StockTakeResponse response) {
    List<StockTakeDetailResponse> stockTakeDetailResponses = new ArrayList<>();
    for (StockTakeDetailResponse detail : response.getStockTakeDetails()) {
      StockTakeDetail stockTakeDetail = stockTakeDetailRepository.findById(
              StockTakeDetailId.builder()
                  .inventoryId(detail.getInventory().getId())
                  .stockTakeId(stockTakeId).build())
          .orElseThrow(() -> new NotFoundException("stoke take detail not found"));
      stockTakeDetail.setActualQuantity(detail.getActualQuantity());
      stockTakeDetail.setDamagedQuantity(detail.getDamagedQuantity());
      stockTakeDetail.setStatus(detail.getStatus());
      stockTakeDetail.setDescription(detail.getDescription());
      StockTakeDetailResponse stockTakeDetailResponse = StockTakeDetailMapper.INSTANCE.toDto(stockTakeDetailRepository.save(stockTakeDetail));
      if (detail.getDamagedProducts().size() != 0 || stockTakeDetail.getDamagedProducts().size() != 0) {

        stockTakeDetailResponse.setDamagedProducts(damagedProductService.updateAndCreateByStockTakeId(stockTakeId, stockTakeDetail.getInventory().getId() ,detail.getDamagedProducts())
            .stream().map((i) -> {
              return DamagedProductWithResponse.builder()
                  .stockTakeCode(i.getStockTakeCode())
                  .transactionCode(i.getTransactionCode())
                  .isExchange(i.isExchange())
                  .id(i.getId())
                  .status(i.getStatus())
                  .quantity(i.getQuantity())
                  .build();
            }).collect(Collectors.toSet()));
      }
      stockTakeDetailResponses.add(stockTakeDetailResponse);
    }
    response.setStockTakeDetails(stockTakeDetailResponses);
    return response;
  }

  @Transactional
  public StockTakeResponse completeStockTake(Long stockTakeId, User user) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stoke take not found"));
    stockTake.setStatus(StockTakeStatus.COMPLETED);
    stockTake.setExecutor(user);
    stockTakeRepository.save(stockTake);
    return StockTakeMapper.INSTANCE.toDto(stockTake);
  }

  @Transactional
  public StockTakeResponse approve(Long stockTakeId, User user) {
    StockTake stockTake = stockTakeRepository.findById(stockTakeId)
        .orElseThrow(() -> new NotFoundException("stoke take not found"));
    stockTake.setStatus(StockTakeStatus.VERIFIED);
    stockTake.setApprover(user);
    Transaction transaction = transactionRepository.save(
        Transaction.builder()
            .code(transactionService.generateTransactionCode())
            .approver(user)
            .creator(user)
            .executor(stockTake.getExecutor())
            .transactionDate(LocalDateTime.now())
            .transactionType(TransactionType.INVENTORY)
            .status(TransactionStatus.COMPLETE)
            .warehouse(stockTake.getWarehouse())
            .description(
                "kiểm kê kho " + stockTake.getWarehouse().getName() + " " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .build());
    Set<TransactionDetail> details = new HashSet<>();
    for (StockTakeDetail stockTakeDetail : stockTake.getStockTakeDetails()) {
      Inventory inventory = stockTakeDetail.getInventory();
      Long actual = Optional.ofNullable(stockTakeDetail.getActualQuantity()).orElse(0L);
      Long damaged = Optional.ofNullable(stockTakeDetail.getDamagedQuantity()).orElse(0L);
      TransactionDetail transactionDetail = TransactionDetail.builder()
          .product(stockTakeDetail.getInventory().getProduct())
          .quantity((int) (actual - damaged))
          .transaction(transaction)
          .transactionType(transaction.getTransactionType())
          .inventory(inventory)
          .build();

      Set<DamagedProduct> damagedProducts = new HashSet<>();

//      for (DamagedProduct damagedProduct : transactionDetail.getDamagedProducts()) {
//        damagedProduct.setStatus(DamagedProductStatus.ACTIVE);
//        damagedProducts.add(damagedProductRepository.save(damagedProduct));
//      }
//      transactionDetail.setDamagedProducts(damagedProducts);
      details.add(transactionDetailRepository.save(transactionDetail));
    }
    transactionRepository.save(transaction);

    stockTakeRepository.save(stockTake);
    return StockTakeMapper.INSTANCE.toDto(stockTake);
  }

  public String generateStockTakeCode() {
    LocalDateTime from = LocalDate.now().atStartOfDay();
    LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);
    int sequence = stockTakeRepository.findTodaySequence(from, to);
    String prefix = "STK";
    String date = java.time.LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
    String number = String.format("%03d", sequence);
    return String.format("%s-%s-%s", prefix, date, number);
  }

}
