package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction;

import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
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
  UserResponse executor;
  UserResponse approver;
  UserResponse creator;
  WarehouseResponse warehouse;
  TransactionStatus status;
  String transferCode;
  String partnerCode;
  List<TransactionDetailResponse> details;

  @Value
  public static class TransactionDetailResponse implements Serializable {

    Long id;
    ProductResponse product;
    InventoryResponse inventory;
    int quantity;
    TransactionType transactionType;
  }
}