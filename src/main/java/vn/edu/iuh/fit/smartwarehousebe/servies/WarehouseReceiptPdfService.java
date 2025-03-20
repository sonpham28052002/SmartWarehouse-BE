package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.WarehouseReceipt;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.WarehouseReceiptRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

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

  public WarehouseReceiptPdfService(PdfGenerationService pdfGenerationService, SupplierService supplierService, WarehouseService warehouseService, UserService userService, ProductService productService) {
    this.pdfGenerationService = pdfGenerationService;
    this.supplierService = supplierService;
    this.warehouseService = warehouseService;
    this.userService = userService;
    this.productService = productService;
  }

 /**
    * Generates a PDF for the given warehouse receipt request.
    *
    * @param request the warehouse receipt request containing the necessary data
    * @return a byte array representing the generated PDF
    */
  public byte[] generatePdf(WarehouseReceiptRequest request) {
    SupplierResponse supplier = supplierService.getById(request.getSupplierId());
    WarehouseResponse warehouse = warehouseService.getById(request.getToWarehouseId());
    User user = userService.getUserById(request.getUserId());
    List<WarehouseReceipt.WarehouseReceiptItem> receiptItems =
        productService.getByIds(request.getProducts().stream().map(WarehouseReceiptRequest.Item::getProductId).toList())
            .stream()
            .map(product -> WarehouseReceipt.WarehouseReceiptItem.builder()
                .productCode(product.getCode())
                .sku(product.getSku())
                .productName(product.getName())
                .unit(product.getUnit().getName())
                .quantity(request.getProducts().stream().filter(p -> p.getProductId().equals(product.getId())).findFirst().get().getQuantity())
                .build())
            .toList();

    WarehouseReceipt receipt = WarehouseReceipt.builder()
        .code(generateWarehouseReceiptCode())
        .supplierCode(supplier.getCode())
        .supplierName(supplier.getName())
        .supplierAddress(supplier.getAddress())
        .supplierPhone(supplier.getPhone())
        .warehouseCode(warehouse.getCode())
        .warehouseName(warehouse.getName())
        .warehouseAddress(warehouse.getAddress())
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