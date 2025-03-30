package vn.edu.iuh.fit.smartwarehousebe.servies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionBetweenRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionDetailRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionExportCsvRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionImportCsvRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse.TransactionDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UnitOfProductNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.PartnerMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionDetailSpecification;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionSpecification;

/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
@Slf4j
@Service
public class TransactionService {

  private final UnitMapper unitMapper;
  private final TransactionRepository transactionRepository;
  private final TransactionDetailRepository transactionDetailRepository;
  private final TransactionMapper transactionMapper;
  private final WarehouseService warehouseService;
  private final PartnerService partnerService;
  private final PartnerMapper partnerMapper;
  private final UserService userService;
  private final ProductMapper productMapper;
  private final CsvService csvService;
  private final WarehouseMapper warehouseMapper;
  private final UnitService unitService;
  private final ProductService productService;
  private final InventoryRepository inventoryRepository;

  public TransactionService(TransactionRepository transactionRepository,
      TransactionMapper transactionMapper, WarehouseService warehouseService,
      PartnerService partnerService, PartnerMapper partnerMapper, UserService userService,
      ProductMapper productMapper, CsvService csvService, WarehouseMapper warehouseMapper,
      UnitService unitService, UnitMapper unitMapper, ProductService productService,
      InventoryRepository inventoryRepository,
      TransactionDetailRepository transactionDetailRepository) {
    this.warehouseService = warehouseService;
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.partnerService = partnerService;
    this.partnerMapper = partnerMapper;
    this.userService = userService;
    this.productMapper = productMapper;
    this.csvService = csvService;
    this.warehouseMapper = warehouseMapper;
    this.unitService = unitService;
    this.unitMapper = unitMapper;
    this.productService = productService;
    this.inventoryRepository = inventoryRepository;
    this.transactionDetailRepository = transactionDetailRepository;

  }

  /**
   * Retrieves a transaction by its ID.
   *
   * @param id the ID of the transaction
   * @return the transaction response
   * @throws TransactionNotFoundException if the transaction is not found
   */
  @Cacheable(value = "transactions", key = "#id")
  public TransactionWithDetailResponse getTransaction(Long id) {
    return transactionRepository.findById(id).map(transactionMapper::toDtoWithDetail)
        .orElseThrow(TransactionNotFoundException::new);
  }

  /**
   * Retrieves a paginated list of transactions based on the provided criteria.
   *
   * @param pageRequest the pagination information
   * @param quest       the criteria for filtering transactions
   * @return a paginated list of transaction responses
   */
  public Page<TransactionResponse> getTransactions(PageRequest pageRequest,
      GetTransactionQuest quest) {
    Specification<Transaction> specification = SpecificationBuilder.<Transaction>builder()
        .with(TransactionSpecification.hasTransactionType(quest.getTransactionType())).with(
            TransactionSpecification.hasTransactionDateBetween(quest.getStartDate(),
                quest.getEndDate()))
        .with(TransactionSpecification.hasTransactionExecutor(quest.getExecutor()))
        .with(TransactionSpecification.hasTransactionWarehouse(quest.getWarehouse()))
        .with(TransactionSpecification.hasTransactionTransfer(quest.getTransfer()))
        .with(TransactionSpecification.hasTransactionPartner(quest.getPartner())).build();

    return transactionRepository.findAll(specification, pageRequest).map(transactionMapper::toDto);
  }

  /**
   * Creates a new transaction with detailed information.
   *
   * @param request the transaction request containing details
   * @return the transaction response
   */
  @CacheEvict(value = "transactions", allEntries = true)
  public TransactionWithDetailResponse createTransaction(TransactionRequest request) {
    Transaction transaction = transactionMapper.toEntity(request);
    // Set the warehouse, transfer, partner, and executor based on the request
    transaction.setWarehouse(
        warehouseMapper.toEntity(warehouseService.getById(request.getWarehouseId())));
    if (request.getTransferId() != null) {
      transaction.setTransfer(
          warehouseMapper.toEntity(warehouseService.getById(request.getTransferId())));
    }
    if (request.getPartnerId() != null) {
      transaction.setPartner(
          partnerMapper.toEntity(partnerService.getById(request.getPartnerId())));
    }
    transaction.setExecutor(userService.getUserById(userService.getCurrentUserId()));
    Set<TransactionDetail> details = request.getDetails().stream().map(d -> {
      TransactionDetail detail = transactionMapper.toEntity(d);
      ProductResponse product = productService.getById(d.getProductId());
      if (!(Objects.equals(product.getUnit().getId(), d.getUnitId()) || product.getConversionUnits()
          .stream().anyMatch(
              convertUnit -> Objects.equals(convertUnit.getFromUnit().getId(), d.getUnitId())))) {
        throw new UnitOfProductNotFoundException();
      }

      Inventory inventory = null;
      if (d.getStorageLocationId() == null) {
        inventory = Inventory.builder().product(productMapper.toEntity(product))
            .quantity((long) d.getQuantity())
            .unit(unitMapper.toEntity(unitService.getUnitById(d.getUnitId()))).build();
      } else {
        inventory = inventoryRepository.findByProduct_IdAndStorageLocation_Id(product.getId(),
                d.getStorageLocationId())
            .orElseThrow(() -> new NoSuchElementException("Inventory not found"));
        if (inventory.getQuantity() < -1 * d.getQuantity()) {
          throw new IllegalArgumentException("Insufficient stock");
        }
        inventory.setQuantity(inventory.getQuantity() + d.getQuantity());

      }

      detail.setProduct(productMapper.toEntity(product));
      detail.setTransactionType(transaction.getTransactionType());
      detail.setTransaction(transaction);
      detail.setInventory(inventory);

      inventoryRepository.save(inventory);
      return detail;
    }).collect(Collectors.toSet());
    transaction.setDetails(details);

    return transactionMapper.toDtoWithDetail(transactionRepository.save(transaction));
  }

  /**
   * Retrieves a paginated list of transactions between the specified start and end dates.
   *
   * @param pageRequest the pagination information
   * @param request     the request containing the start and end dates
   * @return a paginated list of transaction responses
   */
  public Page<TransactionResponse> getTransactionBetween(PageRequest pageRequest,
      GetTransactionBetweenRequest request) {
    LocalDateTime from = request.getStartDate().atStartOfDay();
    LocalDateTime to = request.getEndDate().atTime(23, 59, 59);
    return transactionRepository.findTransactionsByTransactionDateBetween(from, to, pageRequest)
        .map(transactionMapper::toDto);
  }

  /**
   * Imports warehouse transactions from a CSV file.
   *
   * @param file the CSV file containing transaction data
   * @return the transaction response with details
   */
  public TransactionWithDetailResponse importWarehouseTransaction(MultipartFile file) {
    try {
      List<TransactionImportCsvRequest> transactionCSVRequest = csvService.parseCsv(
          file.getInputStream(), TransactionImportCsvRequest.class);
      WarehouseResponse warehouse = warehouseService.getByCode(
          transactionCSVRequest.get(0).getWarehouseCode());
      TransactionRequest request = TransactionRequest.builder().warehouseId(warehouse.getId())
          .description(transactionCSVRequest.get(0).getDescription()).build();
      if (transactionCSVRequest.get(0).getTransferCode() != null) {
        if (transactionCSVRequest.get(0).getPartnerCode() != null) {
          throw new RuntimeException("Cannot import transaction with both transfer and partner");
        }
        if (transactionCSVRequest.get(0).getTransferCode()
            .equals(transactionCSVRequest.get(0).getWarehouseCode())) {
          throw new RuntimeException(
              "Cannot import transaction with transfer code equals to warehouse code");
        }
        WarehouseResponse transfer = warehouseService.getByCode(
            transactionCSVRequest.get(0).getTransferCode());
        request.setTransferId(transfer.getId());
        request.setTransactionType(TransactionType.IMPORT_FROM_WAREHOUSE);
      } else {
        PartnerResponse partner = partnerService.getByCode(
            transactionCSVRequest.get(0).getPartnerCode());
        request.setPartnerId(partner.getId());
        request.setTransactionType(TransactionType.IMPORT_FROM_SUPPLIER);
      }
      List<TransactionRequest.TransactionDetailRequest> details = transactionCSVRequest.stream()
          .map(t -> {
            ProductResponse product = productService.getByCode(t.getProductCode());
            UnitResponse unit = unitService.getUnitByCode(t.getUnitCode());

            return TransactionRequest.TransactionDetailRequest.builder().productId(product.getId())
                .unitId(unit.getId()).quantity(t.getQuantity()).build();
          }).toList();
      request.setDetails(details);

      return createTransaction(request);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public TransactionWithDetailResponse exportWarehouseTransaction(MultipartFile file) {
    try {
      List<TransactionExportCsvRequest> transactionCSVRequest = csvService.parseCsv(
          file.getInputStream(), TransactionExportCsvRequest.class);
      WarehouseResponse warehouse = warehouseService.getByCode(
          transactionCSVRequest.get(0).getWarehouseCode());
      TransactionRequest request = TransactionRequest.builder().warehouseId(warehouse.getId())
          .description(transactionCSVRequest.get(0).getDescription())
          .transactionType(TransactionType.EXPORT_TO_WAREHOUSE).build();
      if (transactionCSVRequest.get(0).getTransferCode()
          .equals(transactionCSVRequest.get(0).getWarehouseCode())) {
        throw new RuntimeException(
            "Cannot import transaction with transfer code equals to warehouse code");
      }
      WarehouseResponse transfer = warehouseService.getByCode(
          transactionCSVRequest.get(0).getTransferCode());
      request.setTransferId(transfer.getId());
      List<TransactionRequest.TransactionDetailRequest> details = transactionCSVRequest.stream()
          .map(t -> {
            ProductResponse product = productService.getByCode(t.getProductCode());
            UnitResponse unit = unitService.getUnitByCode(t.getUnitCode());

            return TransactionRequest.TransactionDetailRequest.builder().productId(product.getId())
                .unitId(unit.getId())
                .quantity(t.getQuantity() * -1) // Export to warehouse, so quantity is negative
                .storageLocationId(t.getStorageLocationId()).build();
          }).toList();
      request.setDetails(details);

      return createTransaction(request);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Page<TransactionDetailResponse> getTransactionDetailByTransactionId(Long transactionId,
      GetTransactionDetailRequest request, PageRequest pageRequest) {

    Specification<TransactionDetail> spec = Specification.where(null);

    if (request.getProductCode() != null) {
      spec = spec.and(TransactionDetailSpecification.hasCode(request.getProductCode()));
    }

    if (request.getProductName() != null) {
      spec = spec.and(TransactionDetailSpecification.hasName(request.getProductName()));
    }

    spec = spec.and(TransactionDetailSpecification.hasTransactionId(transactionId));

    return transactionDetailRepository.findAll(spec, pageRequest)
        .map(TransactionDetailMapper.INSTANCE::toDto);
  }

}
