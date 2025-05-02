package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Set;
import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
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
  String code;
  List<TransactionDetailResponse> details;

  @Value
  public static class TransactionDetailResponse implements Serializable {

    ProductResponse product;
    InventoryResponse inventory;
    int quantity;
    TransactionType transactionType;
    Set<DamagedProductWithResponse> damagedProducts;
    Long transactionId;
  }
}