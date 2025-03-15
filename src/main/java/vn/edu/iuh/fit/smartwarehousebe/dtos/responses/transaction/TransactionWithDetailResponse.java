package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction;

import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location.StorageLocationResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Value
public class TransactionWithDetailResponse implements Serializable {
    Long id;
    TransactionType transactionType;
    LocalDateTime transactionDate;
    String transactionFile;
    String description;
    Long executorId;
    Long warehouseId;
    Long transferId;
    Long supplierId;
    transient List<TransactionDetailResponse> details;

    @Value
    public static class TransactionDetailResponse implements Serializable {
        Long id;
        ProductResponse product;
        StorageLocationResponse storageLocation;
        int quantity;
        TransactionType transactionType;
    }
}