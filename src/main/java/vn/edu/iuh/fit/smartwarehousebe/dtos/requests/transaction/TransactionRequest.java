package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Value
public class TransactionRequest implements Serializable {
    @NotNull(message = "Transaction type cannot be null")
    TransactionType transactionType;
    @NotNull(message = "Transaction date cannot be null")
    LocalDateTime transactionDate;
    String transactionFile;
    String description;
    @NotNull(message = "Executor ID cannot be null")
    Long executorId;
    @NotNull(message = "Warehouse ID cannot be null")
    Long warehouseId;
    Long transferId;
    Long supplierId;
    transient List<TransactionDetailRequest> details;

    @Value
    public static class TransactionDetailRequest implements Serializable {
        @NotNull(message = "Product ID cannot be null")
        Long productId;
        @NotNull(message = "Storage ID cannot be null")
        Long storageId;
        @NotNull(message = "Quantity cannot be null")
        int quantity;
        @NotNull(message = "Transaction type cannot be null")
        TransactionType transactionType;
    }
}