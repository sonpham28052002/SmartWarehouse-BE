package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest implements Serializable {
  String description;

  @NotNull(message = "Warehouse ID cannot be null")
  Long warehouseId;

  Long transferId;

  Long supplierId;

  TransactionType transactionType;

  transient List<TransactionDetailRequest> details;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TransactionDetailRequest implements Serializable {
    @NotNull(message = "Product ID cannot be null")
    Long productId;

    @NotNull(message = "Storage ID cannot be null")
    Long unitId;

    @NotNull(message = "Quantity cannot be null")
    int quantity;
  }
}