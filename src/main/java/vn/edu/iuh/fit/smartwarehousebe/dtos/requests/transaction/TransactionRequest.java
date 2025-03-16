package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.annotations.CsvField;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest implements Serializable {
  @NotNull(message = "Transaction type cannot be null")
  @CsvField("transactionType")
  TransactionType transactionType;

  @NotNull(message = "Transaction date cannot be null")
  @CsvField("transactionDate")
  LocalDateTime transactionDate;

  @CsvField("transactionFile")
  String transactionFile;

  @CsvField("description")
  String description;

  @NotNull(message = "Executor ID cannot be null")
  @CsvField("executorId")
  Long executorId;

  @NotNull(message = "Warehouse ID cannot be null")
  @CsvField("warehouseId")
  Long warehouseId;

  @CsvField("transferId")
  Long transferId;

  @CsvField("supplierId")
  Long supplierId;

  @CsvField("details")
  transient List<TransactionDetailRequest> details;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TransactionDetailRequest implements Serializable {
    @NotNull(message = "Product ID cannot be null")
    @CsvField("productId")
    Long productId;

    @NotNull(message = "Storage ID cannot be null")
    @CsvField("storageId")
    Long storageId;
    @NotNull(message = "Quantity cannot be null")

    @CsvField("quantity")
    int quantity;

    @NotNull(message = "Transaction type cannot be null")
    @CsvField("transactionType")
    TransactionType transactionType;
  }
}