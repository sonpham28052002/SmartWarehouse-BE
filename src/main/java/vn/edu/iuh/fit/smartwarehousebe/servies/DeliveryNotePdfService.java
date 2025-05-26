package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.DeliveryNote;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
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
  private final PartnerService partnerService;

  public DeliveryNotePdfService(PdfGenerationService pdfGenerationService,
      WarehouseService warehouseService, UserService userService, ProductService productService,
      TransactionService transactionService, PartnerService partnerService) {
    this.pdfGenerationService = pdfGenerationService;
    this.warehouseService = warehouseService;
    this.userService = userService;
    this.productService = productService;
    this.transactionService = transactionService;
    this.partnerService = partnerService;
  }

  /**
   * Generates a PDF for the delivery note of a given transaction.
   *
   * @param transactionId the ID of the transaction
   * @return a byte array representing the generated PDF
   */
  public byte[] generatePdf(Long transactionId) {
    TransactionWithDetailResponse transaction = transactionService.getTransaction(transactionId);
    if (transaction.getTransactionType() != TransactionType.EXPORT_FROM_WAREHOUSE
        && transaction.getTransactionType() != TransactionType.WAREHOUSE_TRANSFER) {
      throw new IllegalArgumentException("Transaction type is not export to warehouse");
    }
    WarehouseResponse toWarehouse = null;
    PartnerResponse toPartner = null;
    if (transaction.getTransferCode() != null) {
      toWarehouse = warehouseService.getByCode(transaction.getWarehouse().getCode());
    } else {
      toPartner = partnerService.getByCode(transaction.getPartnerCode());
    }
    UserResponse user = transaction.getCreator() != null ? userService.getUserByCode(
        transaction.getCreator().getCode()) : null;

    List<DeliveryNote.DeliveryNoteItem> noteItems =
        transaction.getDetails()
            .stream()
            .map(item -> {
              ProductResponse product = productService.getByCode(item.getProduct().getCode());
              return DeliveryNote.DeliveryNoteItem.builder()
                  .productCode(item.getProduct().getCode())
                  .sku(product.getSku())
                  .productName(product.getName())
                  .unit(product.getUnit().getName())
                  .quantity(item.getQuantity())
                  .build();
            })
            .toList();

    DeliveryNote deliveryNote = DeliveryNote.builder()
        .code(transaction.getCode() != null ? transaction.getCode() : "")
        .fromWarehouseCode(transaction.getWarehouse().getCode())
        .fromWarehouseName(transaction.getWarehouse().getName())
        .fromWarehouseAddress(transaction.getWarehouse().getAddress())
        .toWarehouseCode(toWarehouse != null ? toWarehouse.getCode() : toPartner.getCode())
        .toWarehouseName(toWarehouse != null ? toWarehouse.getName() : toPartner.getName())
        .toWarehouseAddress(toWarehouse != null ? toWarehouse.getAddress() : toPartner.getAddress())
        .createdBy(user != null ? user.getCode() + " - " + user.getFullName() : "")
        .createdDate(LocalDateTime.now())
        .type(transaction.getTransactionType().name())
        .items(noteItems)
        .build();
    // Create a model map with the delivery note data
    Map<String, Object> model = new HashMap<>();
    model.put("deliveryNote", deliveryNote);

    // Generate the PDF using the HTML template
    return pdfGenerationService.generatePdfFromHtmlTemplate("delivery-note-template", model);
  }
}