package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Value
public class TransactionRequest implements Serializable {
    Long id;
    TransactionType transactionType;
    LocalDateTime transactionDate;
    String transactionFile;
    String description;
    Long executorId;
    Long warehouseId;
    Long transferId;
    Long supplierId;
}