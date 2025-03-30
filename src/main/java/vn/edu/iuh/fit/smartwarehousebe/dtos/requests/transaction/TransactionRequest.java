package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionRequest implements Serializable {

  String description;

  @NotNull(message = "Warehouse ID cannot be null")
  Long warehouseId;

  Long transferId;

  Long partnerId;

  TransactionType transactionType;

  List<TransactionDetailRequest> details;

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

    Long storageLocationId;
  }
}