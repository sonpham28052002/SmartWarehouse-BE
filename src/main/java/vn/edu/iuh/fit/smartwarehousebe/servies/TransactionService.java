package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UserCodeNotValid;
import vn.edu.iuh.fit.smartwarehousebe.mappers.*;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.specifications.SpecificationBuilder;
import vn.edu.iuh.fit.smartwarehousebe.specifications.TransactionSpecification;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
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

  public TransactionService(TransactionRepository transactionRepository,
                            TransactionMapper transactionMapper, WarehouseService warehouseService, SupplierService supplierService,
                            SupplierMapper supplierMapper, UserService userService, ProductService productService,
                            ProductMapper productMapper, StorageLocationService storageLocationService,
                            StorageLocationMapper storageLocationMapper, CsvService csvService,
                            WarehouseMapper warehouseMapper) {
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
  public TransactionResponse createTransaction(TransactionRequest request) {
    Transaction transaction = transactionMapper.toEntity(request);
    // Set the warehouse, transfer, supplier, and executor based on the request
    transaction.setWarehouse(warehouseMapper.toEntity(warehouseService.getByIdV2(request.getWarehouseId())));
    if (request.getTransferId() != null) {
      transaction.setTransfer(warehouseMapper.toEntity(warehouseService.getByIdV2(request.getTransferId())));
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

    return transactionMapper.toDto(transactionRepository.save(transaction));
  }

  @Transactional
  public Integer importTransaction(MultipartFile file) {
    try {
      List<TransactionRequest> requests = csvService.parseCsv(file.getInputStream(), TransactionRequest.class);
      if (requests.isEmpty()) {
        throw new IllegalArgumentException("CSV file is empty");
      }
      requests.forEach(this::createTransaction);
      return requests.size();
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }
}
