package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.WarehouseReceipt;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: vie
 * @date: 16/3/25
 */
@Service
public class WarehouseReceiptPdfService {

  private final PdfGenerationService pdfGenerationService;
  private final SupplierService supplierService;
  private final WarehouseService warehouseService;
  private final UserService userService;
  private final ProductService productService;
  private final TransactionService transactionService;
  private final UnitService unitService;

  public WarehouseReceiptPdfService(PdfGenerationService pdfGenerationService,
      SupplierService supplierService, WarehouseService warehouseService, UserService userService,
      ProductService productService, TransactionService transactionService,
      UnitService unitService) {
    this.pdfGenerationService = pdfGenerationService;
    this.supplierService = supplierService;
    this.warehouseService = warehouseService;
    this.userService = userService;
    this.productService = productService;
    this.transactionService = transactionService;
    this.unitService = unitService;
  }

  /**
   * Generates a PDF for a warehouse receipt based on the given transaction ID.
   *
   * @param transactionId the ID of the transaction to generate the PDF for
   * @return a byte array representing the generated PDF
   */
  public byte[] generatePdf(Long transactionId) {
    TransactionWithDetailResponse transaction = transactionService.getTransaction(transactionId);
    if (transaction.getTransactionType() != TransactionType.IMPORT_FROM_SUPPLIER &&
        transaction.getTransactionType() != TransactionType.IMPORT_FROM_WAREHOUSE) {
      throw new IllegalArgumentException(
          "Transaction type is not import from supplier or warehouse");
    }

    WarehouseResponse fromWarehouse = null;
    SupplierResponse fromSupplier = null;
    if (transaction.getTransferCode() != null) {
      fromWarehouse = warehouseService.getByCode(transaction.getTransferCode());
    } else {
      fromSupplier = supplierService.getByCode(transaction.getSupplierCode());
    }
    WarehouseResponse toWarehouse = warehouseService.getByCode(
        transaction.getWarehouse().getCode());
    UserResponse user = userService.getUserByCode(transaction.getExecutorCode());
    List<WarehouseReceipt.WarehouseReceiptItem> receiptItems =
        transaction.getDetails()
            .stream()
            .map(
                item -> {
                  ProductResponse product = productService.getByCode(item.getProductCode());
                  UnitResponse unit = item.getInventory().getUnit();
                  return WarehouseReceipt.WarehouseReceiptItem.builder()
                      .productCode(product.getCode())
                      .productName(product.getName())
                      .sku(product.getSku())
                      .quantity(item.getQuantity())
                      .unit(unit.getName())
                      .build();
                })
            .toList();

    WarehouseReceipt receipt = WarehouseReceipt.builder()
        .code(generateWarehouseReceiptCode())
        .supplierCode(fromWarehouse != null ? fromWarehouse.getCode() : fromSupplier.getCode())
        .supplierName(fromWarehouse != null ? fromWarehouse.getName() : fromSupplier.getName())
        .supplierAddress(
            fromWarehouse != null ? fromWarehouse.getAddress() : fromSupplier.getAddress())
        .supplierPhone(fromWarehouse != null ? "" : fromSupplier.getPhone())
        .warehouseCode(toWarehouse.getCode())
        .warehouseName(toWarehouse.getName())
        .warehouseAddress(toWarehouse.getAddress())
        .createdBy(user.getFullName())
        .createdDate(LocalDateTime.now())
        .items(receiptItems)
        .build();

    // Create a model map with the warehouse receipt data
    Map<String, Object> model = new HashMap<>();
    model.put("warehouseReceipt", receipt);

    // Generate the PDF using the HTML template
    return pdfGenerationService.generatePdfFromHtmlTemplate("warehouse-receipt-template", model);
  }

  private String generateWarehouseReceiptCode() {
    // Generate a unique code for the warehouse receipt
    return "WR-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
  }
}