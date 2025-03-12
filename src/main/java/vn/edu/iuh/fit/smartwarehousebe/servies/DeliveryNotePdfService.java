package vn.edu.iuh.fit.smartwarehousebe.servies;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.DeliveryNote;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.DeliveryNoteRequest;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for generating PDF documents for delivery notes
 */
@Service
public class DeliveryNotePdfService {

    private final PdfGenerationService pdfGenerationService;
    private final WarehouseService warehouseService;
    private final UserService userService;
    private final ProductService productService;

    public DeliveryNotePdfService(PdfGenerationService pdfGenerationService, WarehouseService warehouseService, UserService userService, ProductService productService) {
        this.pdfGenerationService = pdfGenerationService;
        this.warehouseService = warehouseService;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Generates a PDF document for a delivery note
     *
     * @param request The delivery note request containing information about the delivery
     * @return A byte array containing the generated PDF
     */
    public byte[] generatePdf(DeliveryNoteRequest request) {
        Warehouse fromWarehouse = warehouseService.getById(request.getFromWarehouseId());
        Warehouse toWarehouse = warehouseService.getById(request.getToWarehouseId());
        User user = userService.getUserById(request.getUserId());
        List<DeliveryNote.DeliveryNoteItem> noteItems =
                productService.getByIds(request.getProducts().stream().map(DeliveryNoteRequest.Item::getProductId).toList())
                        .stream()
                        .map(product -> DeliveryNote.DeliveryNoteItem.builder()
                                .productCode(product.getCode())
                                .sku(product.getSku())
                                .productName(product.getName())
                                .unit(product.getUnit().getName())
                                .quantity(request.getProducts().stream().filter(p -> p.getProductId().equals(product.getId())).findFirst().get().getQuantity())
                                .build())
                        .toList();

        DeliveryNote deliveryNote = DeliveryNote.builder()
                .code(generateDeliveryNoteCode())
                .fromWarehouseCode(fromWarehouse.getCode())
                .fromWarehouseName(fromWarehouse.getName())
                .fromWarehouseAddress(fromWarehouse.getAddress())
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