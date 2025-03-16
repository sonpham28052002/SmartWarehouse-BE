package vn.edu.iuh.fit.smartwarehousebe.servies;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionBetweenRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.WarehouseReceiptRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.*;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionSpecification;

import java.time.LocalDateTime;
import java.util.List;
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
  private final TransactionRepository transactionRepository;
  private final TransactionMapper transactionMapper;
  private final WarehouseService warehouseService;
  private final SupplierService supplierService;
  private final SupplierMapper supplierMapper;
  private final UserService userService;
  private final ProductService productService;
  private final ProductMapper productMapper;
  private final StorageLocationService storageLocationService;
  private final StorageLocationMapper storageLocationMapper;
  private final CsvService csvService;
  private final WarehouseMapper warehouseMapper;
  private final WarehouseReceiptPdfService warehouseReceiptPdfService;
  private final StorageService storageService;

  public TransactionService(TransactionRepository transactionRepository,
                            TransactionMapper transactionMapper, WarehouseService warehouseService, SupplierService supplierService,
                            SupplierMapper supplierMapper, UserService userService, ProductService productService,
                            ProductMapper productMapper, StorageLocationService storageLocationService,
                            StorageLocationMapper storageLocationMapper, CsvService csvService,
                            WarehouseMapper warehouseMapper, WarehouseReceiptPdfService warehouseReceiptPdfService, StorageService storageService) {
    this.warehouseService = warehouseService;
    this.transactionRepository = transactionRepository;
    this.transactionMapper = transactionMapper;
    this.supplierService = supplierService;
    this.supplierMapper = supplierMapper;
    this.userService = userService;
    this.productService = productService;
    this.productMapper = productMapper;
    this.storageLocationService = storageLocationService;
    this.storageLocationMapper = storageLocationMapper;
    this.csvService = csvService;
    this.warehouseMapper = warehouseMapper;
    this.warehouseReceiptPdfService = warehouseReceiptPdfService;
    this.storageService = storageService;
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
    if (request.getExecutorId() != null) {
      transaction.setExecutor(userService.getUserById(request.getExecutorId()));
    }
    Set<TransactionDetail> details = request.getDetails().stream()
        .map(d -> {
          TransactionDetail detail = transactionMapper.toEntity(d);
          detail.setProduct(productMapper.toEntity(productService.getById(d.getProductId())));
          detail.setStorageLocation(storageLocationMapper.toEntity(storageLocationService.getById(d.getStorageId())));
          detail.setTransaction(transaction);
          return detail;
        })
        .collect(Collectors.toSet());
    transaction.setDetails(details);

    return transactionMapper.toDtoWithDetail(transactionRepository.save(transaction));
  }

  /**
   * Retrieves a paginated list of transactions that occurred between the specified start and end dates.
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

  public TransactionWithDetailResponse importWarehouseTransaction(MultipartFile file) {
    try {
      List<TransactionRequest> requests = csvService.parseCsv(file.getInputStream(), TransactionRequest.class);
      if (requests.isEmpty()) {
        throw new IllegalArgumentException("CSV file is empty");
      }
      TransactionRequest transaction = requests.get(0);
      WarehouseReceiptRequest request = WarehouseReceiptRequest.builder()
          .toWarehouseId(transaction.getWarehouseId())
          .supplierId(transaction.getSupplierId())
          .userId(transaction.getExecutorId())
          .products(transaction.getDetails().stream().map(detail -> WarehouseReceiptRequest.Item.builder()
              .productId(detail.getProductId())
              .quantity(detail.getQuantity())
              .build()).toList())
          .build();
      MultipartFile pdfContent = warehouseReceiptPdfService.generatePdf(request);
      String url = "";
//      String url = storageService.uploadFile(pdfContent);
      transaction.setTransactionFile(url);
      return createTransaction(transaction);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
}
