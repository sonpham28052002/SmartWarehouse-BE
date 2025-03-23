package vn.edu.iuh.fit.smartwarehousebe.servies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UnitOfProductNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.*;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionSpecification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
  private final TransactionMapper transactionMapper;
  private final WarehouseService warehouseService;
  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;
  private final UserService userService;
  private final ProductMapper productMapper;
  private final CsvService csvService;
  private final WarehouseMapper warehouseMapper;
  private final UnitService unitService;
  private final ProductService productService;

  public TransactionService(TransactionRepository transactionRepository,
                            TransactionMapper transactionMapper, WarehouseService warehouseService, SupplierService supplierService,
                            SupplierMapper supplierMapper, UserService userService,
                            ProductMapper productMapper,
                            CsvService csvService,
                            WarehouseMapper warehouseMapper, UnitService unitService,
                            UnitMapper unitMapper, ProductService productService) {
    this.warehouseService = warehouseService;
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.supplierService = supplierService;
    this.supplierMapper = supplierMapper;
    this.userService = userService;
    this.productMapper = productMapper;
    this.csvService = csvService;
    this.warehouseMapper = warehouseMapper;
    this.unitService = unitService;
    this.unitMapper = unitMapper;
    this.productService = productService;
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
  public Page<TransactionResponse> getTransactions(PageRequest pageRequest, GetTransactionQuest quest) {
    Specification<Transaction> specification = SpecificationBuilder.<Transaction>builder()
        .with(TransactionSpecification.hasTransactionType(quest.getTransactionType()))
        .with(TransactionSpecification.hasTransactionDate(quest.getTransactionDate()))
        .with(TransactionSpecification.hasTransactionExecutor(quest.getExecutor()))
        .with(TransactionSpecification.hasTransactionWarehouse(quest.getWarehouse()))
        .with(TransactionSpecification.hasTransactionTransfer(quest.getTransfer()))
        .with(TransactionSpecification.hasTransactionSupplier(quest.getSupplier()))
        .build();

    return transactionRepository.findAll(specification, pageRequest)
        .map(transactionMapper::toDto);
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
    // Set the warehouse, transfer, supplier, and executor based on the request
    transaction.setWarehouse(warehouseMapper.toEntity(warehouseService.getById(request.getWarehouseId())));
    if (request.getTransferId() != null) {
      transaction.setTransfer(warehouseMapper.toEntity(warehouseService.getById(request.getTransferId())));
    }
    if (request.getSupplierId() != null) {
      transaction.setSupplier(supplierMapper.toEntity(supplierService.getById(request.getSupplierId())));
    }
    transaction.setExecutor(userService.getUserById(userService.getCurrentUserId()));
    Set<TransactionDetail> details = request.getDetails().stream()
        .map(d -> {
          TransactionDetail detail = transactionMapper.toEntity(d);
          ProductResponse product = productService.getById(d.getProductId());
          if (!(Objects.equals(product.getUnit().getId(), d.getUnitId()) || product.getConversionUnits().stream().anyMatch(
              convertUnit -> Objects.equals(convertUnit.getToUnit().getId(), d.getUnitId())
          ))) {
            throw new UnitOfProductNotFoundException();
          }

          detail.setProduct(productMapper.toEntity(product));
          detail.setTransactionType(transaction.getTransactionType());
          detail.setUnit(unitMapper.toEntity(unitService.getUnitById(d.getUnitId())));
          detail.setTransaction(transaction);
          return detail;
        })
        .collect(Collectors.toSet());
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
  public Page<TransactionResponse> getTransactionBetween(PageRequest pageRequest, GetTransactionBetweenRequest request) {
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
      List<TransactionImportCsvRequest> transactionCSVRequest = csvService.parseCsv(file.getInputStream(), TransactionImportCsvRequest.class);
      WarehouseResponse warehouse = warehouseService.getByCode(transactionCSVRequest.get(0).getWarehouseCode());
      TransactionRequest request = TransactionRequest.builder()
          .warehouseId(warehouse.getId())
          .description(transactionCSVRequest.get(0).getDescription())
          .build();
      if (transactionCSVRequest.get(0).getTransferCode() != null) {
        if (transactionCSVRequest.get(0).getSupplierCode() != null) {
          throw new RuntimeException("Cannot import transaction with both transfer and supplier");
        }
        if (transactionCSVRequest.get(0).getTransferCode().equals(transactionCSVRequest.get(0).getWarehouseCode())) {
          throw new RuntimeException("Cannot import transaction with transfer code equals to warehouse code");
        }
        WarehouseResponse transfer = warehouseService.getByCode(transactionCSVRequest.get(0).getTransferCode());
        request.setTransferId(transfer.getId());
        request.setTransactionType(TransactionType.IMPORT_FROM_WAREHOUSE);
      } else {
        SupplierResponse supplier = supplierService.getByCode(transactionCSVRequest.get(0).getSupplierCode());
        request.setSupplierId(supplier.getId());
        request.setTransactionType(TransactionType.IMPORT_FROM_SUPPLIER);
      }
      List<TransactionRequest.TransactionDetailRequest> details = transactionCSVRequest.stream()
          .map(t -> {
            ProductResponse product = productService.getByCode(t.getProductCode());
            UnitResponse unit = unitService.getUnitByCode(t.getUnitCode());

            return TransactionRequest.TransactionDetailRequest.builder()
                .productId(product.getId())
                .unitId(unit.getId())
                .quantity(t.getQuantity())
                .build();
          })
          .toList();
      request.setDetails(details);

      return createTransaction(request);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public TransactionWithDetailResponse exportWarehouseTransaction(MultipartFile file) {
    try {
      List<TransactionExportCsvRequest> transactionCSVRequest = csvService.parseCsv(file.getInputStream(), TransactionExportCsvRequest.class);
      WarehouseResponse warehouse = warehouseService.getByCode(transactionCSVRequest.get(0).getWarehouseCode());
      TransactionRequest request = TransactionRequest.builder()
          .warehouseId(warehouse.getId())
          .description(transactionCSVRequest.get(0).getDescription())
          .transactionType(TransactionType.EXPORT_TO_WAREHOUSE)
          .build();
      if (transactionCSVRequest.get(0).getTransferCode().equals(transactionCSVRequest.get(0).getWarehouseCode())) {
        throw new RuntimeException("Cannot import transaction with transfer code equals to warehouse code");
      }
      WarehouseResponse transfer = warehouseService.getByCode(transactionCSVRequest.get(0).getTransferCode());
      request.setTransferId(transfer.getId());
      List<TransactionRequest.TransactionDetailRequest> details = transactionCSVRequest.stream()
          .map(t -> {
            ProductResponse product = productService.getByCode(t.getProductCode());
            UnitResponse unit = unitService.getUnitByCode(t.getUnitCode());

            return TransactionRequest.TransactionDetailRequest.builder()
                .productId(product.getId())
                .unitId(unit.getId())
                .quantity(t.getQuantity() * -1) // Export to warehouse, so quantity is negative
                .build();
          })
          .toList();
      request.setDetails(details);

      return createTransaction(request);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
