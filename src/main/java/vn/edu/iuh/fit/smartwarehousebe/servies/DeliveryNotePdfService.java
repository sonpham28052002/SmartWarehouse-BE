package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.DeliveryNote;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
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
public class DeliveryNotePdfService {

  private final PdfGenerationService pdfGenerationService;
  private final WarehouseService warehouseService;
  private final UserService userService;
  private final ProductService productService;
  private final TransactionService transactionService;
  private final SupplierService supplierService;

  public DeliveryNotePdfService(PdfGenerationService pdfGenerationService, WarehouseService warehouseService, UserService userService, ProductService productService, TransactionService transactionService, SupplierService supplierService) {
    this.pdfGenerationService = pdfGenerationService;
    this.warehouseService = warehouseService;
    this.userService = userService;
    this.productService = productService;
    this.transactionService = transactionService;
    this.supplierService = supplierService;
  }

  /**
   * Generates a PDF for the delivery note of a given transaction.
   *
   * @param transactionId the ID of the transaction
   * @return a byte array representing the generated PDF
   */
  public byte[] generatePdf(Long transactionId) {
    TransactionWithDetailResponse transaction = transactionService.getTransaction(transactionId);
    if (transaction.getTransactionType() != TransactionType.EXPORT_TO_WAREHOUSE) {
      throw new IllegalArgumentException("Transaction type is not export to warehouse");
    }
    WarehouseResponse fromWarehouse = null;
    SupplierResponse fromSupplier = null;
    if (transaction.getTransferCode() != null) {
      fromWarehouse = warehouseService.getByCode(transaction.getTransferCode());
    } else {
      fromSupplier = supplierService.getByCode(transaction.getSupplierCode());
    }
    WarehouseResponse toWarehouse = warehouseService.getByCode(transaction.getWarehouseCode());
    UserResponse user = userService.getUserByCode(transaction.getExecutorCode());

    List<DeliveryNote.DeliveryNoteItem> noteItems =
        transaction.getDetails()
            .stream()
            .map(item -> {
              ProductResponse product = productService.getByCode(item.getProductCode());
              return DeliveryNote.DeliveryNoteItem.builder()
                  .productCode(item.getProductCode())
                  .sku(product.getSku())
                  .productName(product.getName())
                  .unit(product.getUnit().getName())
                  .quantity(item.getQuantity())
                  .build();
            })
            .toList();

    DeliveryNote deliveryNote = DeliveryNote.builder()
        .code(generateDeliveryNoteCode())
        .fromWarehouseCode(fromWarehouse != null ? fromWarehouse.getCode() : fromSupplier.getCode())
        .fromWarehouseName(fromWarehouse != null ? fromWarehouse.getName() : fromSupplier.getName())
        .fromWarehouseAddress(fromWarehouse != null ? fromWarehouse.getAddress() : fromSupplier.getAddress())
        .toWarehouseCode(toWarehouse.getCode())
        .toWarehouseName(toWarehouse.getName())
        .toWarehouseAddress(toWarehouse.getAddress())
        .createdBy(user.getFullName())
        .createdDate(LocalDateTime.now())
        .items(noteItems)
        .build();
    // Create a model map with the delivery note data
    Map<String, Object> model = new HashMap<>();
    model.put("deliveryNote", deliveryNote);

    // Generate the PDF using the HTML template
    return pdfGenerationService.generatePdfFromHtmlTemplate("delivery-note-template", model);
  }

  private String generateDeliveryNoteCode() {
    // Implement your logic to generate a unique delivery note code
    // For example, you can use a combination of date and time or a sequence number
    return "DN-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
  }


}