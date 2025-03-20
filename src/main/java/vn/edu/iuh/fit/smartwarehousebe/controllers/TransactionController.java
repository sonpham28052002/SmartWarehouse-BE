package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionBetweenRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.GetTransactionQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction.TransactionRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.servies.DeliveryNotePdfService;
import vn.edu.iuh.fit.smartwarehousebe.servies.TransactionService;

/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService transactionService;
  private final DeliveryNotePdfService deliveryNotePdfService;

  public TransactionController(TransactionService transactionService, DeliveryNotePdfService deliveryNotePdfService) {
    this.transactionService = transactionService;
    this.deliveryNotePdfService = deliveryNotePdfService;
  }

  @GetMapping()
  public ResponseEntity<Page<TransactionResponse>> getPageTransaction(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetTransactionQuest quest
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
    return ResponseEntity.ok(transactionService.getTransactions(pageRequest, quest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TransactionWithDetailResponse> getTransactionById(@PathVariable Long id) {
    return ResponseEntity.ok(transactionService.getTransaction(id));
  }

  @PostMapping("/create")
  public ResponseEntity<TransactionWithDetailResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
    return ResponseEntity.ok(transactionService.createTransaction(request));
  }

  @GetMapping("/between")
  public ResponseEntity<Page<TransactionResponse>> getTransactionBetween(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      GetTransactionBetweenRequest request
  ) {
    Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
    PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
    return ResponseEntity.ok(transactionService.getTransactionBetween(pageRequest, request));
  }

  /**
   * Import transactions from a CSV file.
   * <p>
   * The CSV file should have the following header format:
   * description,warehouse_code,transfer_code,supplier_code,product_code,quantity,unit_code
   * <p>
   * Where:
   * <p>
   * - description: A brief description of the transaction
   * - warehouse_code: The code of the warehouse where the transaction takes place
   * - transfer_code: (Optional) The code of the transfer warehouse if applicable
   * - supplier_code: (Optional) The code of the supplier if applicable
   * - product_code: The code of the product
   * - quantity: The quantity of the product
   * - unit_code: The code of the unit of measurement
   * <p>
   * Multiple products can be included as separate rows with the same warehouse, transfer, and supplier codes.
   * <p>
   * Example:
   * description,warehouse_code,transfer_code,supplier_code,product_code,quantity,unit_code
   * "Nhập hàng từ Công ty TNHH ABC",WH-94636,,SUP-64173,PROD-01351,100,UNIT-18590
   * "Nhập hàng từ Công ty TNHH ABC",WH-94636,,SUP-64173,PROD-39508,200,UNIT-06567
   *
   * @param file the CSV file to import
   * @return the imported transaction
   */
  @PostMapping(value = "/import", consumes = {"multipart/form-data"})
  public TransactionWithDetailResponse importTransaction(@RequestParam("file") MultipartFile file) {
    return transactionService.importWarehouseTransaction(file);
  }

  @PostMapping("/get-warehouse-report/{transactionId}")
  public ResponseEntity<byte[]> exportPdf(@PathVariable Long transactionId) {
    byte[] pdfContent = deliveryNotePdfService.generatePdf(transactionId);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "phieu-nhap-kho-" + transactionId + ".pdf");
    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

    return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
  }
}
