package vn.edu.iuh.fit.smartwarehousebe.servies;

import com.amazonaws.services.kms.model.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.Ids.TransactionDetailId;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionBetweenRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionDetailRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionExportCsvRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionImportCsvRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse.TransactionDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.TransactionNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.exceptions.UnitOfProductNotFoundException;
import vn.edu.iuh.fit.smartwarehousebe.mappers.PartnerMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionDetailMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.TransactionMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.WarehouseMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.models.Transaction;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;
import vn.edu.iuh.fit.smartwarehousebe.repositories.DamagedProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.InventoryRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.ProductRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.StorageLocationRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionDetailRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.TransactionRepository;
import vn.edu.iuh.fit.smartwarehousebe.repositories.WarehouseShelfRepository;
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

  private final DamagedProductRepository damagedProductRepository;

  private final WarehouseShelfRepository warehouseShelfRepository;

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

  private final StorageLocationRepository storageLocationRepository;

  private final ProductRepository productRepository;

  private DamagedProductService damagedProductService;

  public TransactionService(TransactionRepository transactionRepository,
      TransactionMapper transactionMapper, WarehouseService warehouseService,
      PartnerService partnerService, PartnerMapper partnerMapper, UserService userService,
      ProductMapper productMapper, CsvService csvService, WarehouseMapper warehouseMapper,
      UnitService unitService, UnitMapper unitMapper, ProductService productService,
      InventoryRepository inventoryRepository, ProductRepository productRepository,
      TransactionDetailRepository transactionDetailRepository,
      StorageLocationRepository storageLocationRepository,
      WarehouseShelfRepository warehouseShelfRepository,
      DamagedProductService damagedProductService,
      DamagedProductRepository damagedProductRepository) {
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
    this.productRepository = productRepository;
    this.storageLocationRepository = storageLocationRepository;
    this.warehouseShelfRepository = warehouseShelfRepository;
    this.damagedProductRepository = damagedProductRepository;
    this.damagedProductService = damagedProductService;
  }

  /**
   * Retrieves a transaction by its ID.
   *
   * @param id the ID of the transaction
   * @return the transaction response
   * @throws TransactionNotFoundException if the transaction is not found
   */
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
        .with(TransactionSpecification.hasStatus(quest.getStatus()))
        .with(TransactionSpecification.hasTransactionPartner(quest.getPartner()))
        .with(TransactionSpecification.hasCode(quest.getCode()))
        .with(TransactionSpecification.hasTransactionWarehouseCode(quest.getWarehouseCode()))
        .with(TransactionSpecification.hasTransactionWarehouseName(quest.getWarehouseName()))
        .with(TransactionSpecification.hasTransactionPartner(quest.getPartner())).build();

    return transactionRepository.findAll(specification, pageRequest).map(transactionMapper::toDto);
  }

  /**
   * Creates a new transaction with detailed information.
   *
   * @param request the transaction request containing details
   * @return the transaction response
   */
  @Transactional
  public TransactionWithDetailResponse createTransaction(TransactionRequest request) {
    try {

      Transaction transaction = transactionMapper.toEntity(request);
      transaction.setTransactionType(request.getTransactionType());
      transaction.setCode(generateTransactionCode());
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
      transaction.setCreator(userService.getUserById(userService.getCurrentUserId()));
      Set<TransactionDetail> details = request.getDetails().stream().map(d -> {
        TransactionDetail detail = transactionMapper.toEntity(d);
        ProductResponse product = productService.getById(d.getProductId());
        if (!(Objects.equals(product.getUnit().getId(), d.getUnitId())
            || product.getConversionUnits().stream().anyMatch(
            convertUnit -> Objects.equals(convertUnit.getFromUnit().getId(), d.getUnitId())))) {
          throw new UnitOfProductNotFoundException();
        }

        Inventory inventory = null;
        if (d.getStorageLocationName() == null) {
          inventory = Inventory.builder().product(productMapper.toEntity(product))
              .inventoryQuantity ((long) d.getQuantity())
              .unit(unitMapper.toEntity(unitService.getUnitById(d.getUnitId()))).build();
        } else {
          if (request.getTransactionType() == TransactionType.EXPORT_FROM_WAREHOUSE) {
            inventory = inventoryRepository.findByProduct_IdAndStorageLocation_NameAndUnitIdAndStatus(
                    product.getId(), d.getStorageLocationName(), d.getUnitId(), InventoryStatus.ACTIVE)
                .orElseThrow(() -> new NoSuchElementException("Inventory not found"));
          } else {
            Optional<Inventory> inventoryOp = inventoryRepository.findByProduct_IdAndStorageLocation_NameAndUnitIdAndStatus(
                product.getId(), d.getStorageLocationName(), d.getUnitId(), InventoryStatus.ACTIVE);

            if (inventoryOp.isPresent()) {
              inventory = inventoryOp.get();
            } else {
              StorageLocation storageLocation = null;
              Optional<StorageLocation> storageLocationOP = storageLocationRepository.findByName(
                  d.getStorageLocationName());
              if (storageLocationOP.isPresent()) {
                storageLocation = storageLocationOP.get();
              } else {
                String shelfName = d.getStorageLocationName().split("-")[0];
                Long rowIndex = Long.parseLong(d.getStorageLocationName().split("-")[1]);
                Long columnIndex = Long.parseLong(d.getStorageLocationName().split("-")[2]);
                WarehouseShelf warehouseShelf = warehouseShelfRepository.findByShelfName(shelfName);
                storageLocation = storageLocationRepository.save(
                    StorageLocation.builder().maxCapacity(warehouseShelf.getMaxCapacity())
                        .warehouseShelf(warehouseShelf).name(d.getStorageLocationName())
                        .columnIndex(columnIndex).rowIndex(rowIndex).build());
              }
              inventory = inventoryRepository.save(
                  Inventory.builder().status(InventoryStatus.INACTIVE).inventoryQuantity(0L)
                      .storageLocation(storageLocation)
                      .product(productRepository.findById(d.getProductId()).get())
                      .unit(Unit.builder().id(d.getUnitId()).build()).build());
            }
            if (inventory.getQuantity() < -1 * d.getQuantity()) {
              throw new IllegalArgumentException("Insufficient stock");
            }
          }
        }
        Inventory inventoryRes = inventoryRepository.save(inventory);
        TransactionDetailId transactionDetailId = TransactionDetailId.builder()
            .transactionId(transaction.getId()).inventoryId(inventoryRes.getId()).build();
        detail.setId(transactionDetailId);
        detail.setProduct(productMapper.toEntity(product));
        detail.setTransactionType(transaction.getTransactionType());
        detail.setTransaction(transaction);
        detail.setInventory(inventoryRes);

        return detail;
      }).collect(Collectors.toSet());
      transaction.setDetails(details);

      return transactionMapper.toDtoWithDetail(transactionRepository.save(transaction));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return null;
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
                .unitId(unit.getId()).quantity(Long.valueOf(t.getQuantity())).build();
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
          .description(transactionCSVRequest.get(0).getDescription()).build();

      if (transactionCSVRequest.get(0).getPartnerCode() != null) {
        PartnerResponse partner = partnerService.getByCode(
            transactionCSVRequest.get(0).getPartnerCode());
        request.setTransactionType(TransactionType.EXPORT_FROM_WAREHOUSE);
        request.setPartnerId(partner.getId());
      }
      if (transactionCSVRequest.get(0).getTransferCode() != null) {
        if (transactionCSVRequest.get(0).getTransferCode()
            .equals(transactionCSVRequest.get(0).getWarehouseCode())) {
          throw new RuntimeException(
              "Cannot import transaction with transfer code equals to warehouse code");
        } else {
          WarehouseResponse transfer = warehouseService.getByCode(
              transactionCSVRequest.get(0).getTransferCode());
          request.setTransactionType(TransactionType.WAREHOUSE_TRANSFER);
          request.setTransferId(transfer.getId());
        }
      }

      List<TransactionRequest.TransactionDetailRequest> details = transactionCSVRequest.stream()
          .map(t -> {
            ProductResponse product = productService.getByCode(t.getProductCode());
            UnitResponse unit = unitService.getUnitByCode(t.getUnitCode());

            return TransactionRequest.TransactionDetailRequest.builder().productId(product.getId())
                .unitId(unit.getId()).quantity(
                    (long) (t.getQuantity() * -1)) // Export to warehouse, so quantity is negative
                .storageLocationName(t.getStorageLocationName()).build();
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

  public List<TransactionResponse> getTransactions(GetTransactionQuest quest) {
    Specification<Transaction> specification = SpecificationBuilder.<Transaction>builder()
        .with(TransactionSpecification.hasTransactionType(quest.getTransactionType())).with(
            TransactionSpecification.hasTransactionDateBetween(quest.getStartDate(),
                quest.getEndDate()))
        .with(TransactionSpecification.hasTransactionExecutor(quest.getExecutor()))
        .with(TransactionSpecification.hasTransactionWarehouse(quest.getWarehouse()))
        .with(TransactionSpecification.hasTransactionTransfer(quest.getTransfer()))
        .with(TransactionSpecification.hasStatus(quest.getStatus()))
        .with(TransactionSpecification.hasCode(quest.getCode()))
        .with(TransactionSpecification.hasTransactionPartner(quest.getPartner())).build();

    return transactionRepository.findAll(specification).stream()
        .map((i) -> transactionMapper.toDto(i)).toList();
  }

  @Transactional
  public TransactionResponse approve(Long transactionId, User user) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction not found"));
    for (TransactionDetail detail : transaction.getDetails()) {
      Inventory inventory = detail.getInventory();
      inventory.setStatus(InventoryStatus.ACTIVE);
      inventoryRepository.save(inventory);
    }
    transaction.setStatus(TransactionStatus.COMPLETE);
    transaction.setApprover(user);
    return transactionMapper.toDto(transactionRepository.save(transaction));
  }

  public String generateTransactionCode() {
    LocalDateTime from = LocalDate.now().atStartOfDay();
    LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);
    int sequence = transactionRepository.findTodaySequence(from, to);
    String prefix = "TRANS";
    String date = java.time.LocalDate.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
    String number = String.format("%03d", sequence);
    return String.format("%s-%s-%s", prefix, date, number);
  }

  @Transactional
  public TransactionWithDetailResponse save(Long transactionId,
      TransactionWithDetailResponse transactionWithDetailResponse, User user) {

    for (TransactionDetailResponse detail : transactionWithDetailResponse.getDetails()) {
      TransactionDetailId transactionDetailId = TransactionDetailId.builder()
          .transactionId(transactionId).inventoryId(detail.getInventory().getId()).build();
      TransactionDetail transactionDetail = transactionDetailRepository.findById(
          transactionDetailId).get();
      transactionDetail.setActualQuantity(detail.getActualQuantity());
      damagedProductService.updateAndCreateByTransactionId(transactionId,
          detail.getInventory().getId(), detail.getDamagedProducts());
    }
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction not found"));
    transaction.setExecutor(user);
    return transactionMapper.toDtoWithDetail(transactionRepository.save(transaction));
  }

  @Transactional
  public TransactionWithDetailResponse complete(Long transactionId,
      TransactionWithDetailResponse transactionWithDetailResponse, User user) {

    for (TransactionDetailResponse detail : transactionWithDetailResponse.getDetails()) {
      TransactionDetailId transactionDetailId = TransactionDetailId.builder()
          .transactionId(transactionId).inventoryId(detail.getInventory().getId()).build();
      TransactionDetail transactionDetail = transactionDetailRepository.findById(
          transactionDetailId).get();
      transactionDetail.setActualQuantity(detail.getActualQuantity());
      damagedProductService.updateAndCreateByTransactionId(transactionId,
          detail.getInventory().getId(), detail.getDamagedProducts());
    }
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction not found"));
    transaction.setStatus(TransactionStatus.PENDING_APPROVAL);
    transaction.setExecutor(user);
    return transactionMapper.toDtoWithDetail(transactionRepository.save(transaction));
  }

  @Transactional
  public TransactionWithDetailResponse start(Long transactionId, User user) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Transaction not found"));
    transaction.setStatus(TransactionStatus.IN_PROCESS);
    transaction.setExecutor(user);
    return transactionMapper.toDtoWithDetail((transactionRepository.save(transaction)));
  }

}

