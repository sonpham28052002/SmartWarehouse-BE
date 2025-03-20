package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction;

import lombok.Value;
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
  String executorCode;
  String warehouseCode;
  String transferCode;
  String supplierCode;
  transient List<TransactionDetailResponse> details;

  @Value
  public static class TransactionDetailResponse implements Serializable {
    Long id;
    String productCode;
    String storageLocationId;
    int quantity;
    TransactionType transactionType;
    String unitCode;
  }
}