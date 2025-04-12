package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.edu.iuh.fit.smartwarehousebe.models.Transaction}
 */
@Value
public class TransactionResponse implements Serializable {

  Long id;
  TransactionType transactionType;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime transactionDate;
  String transactionFile;
  String description;
  UserResponse executor;
  UserResponse creator;
  UserResponse approver;
  WarehouseResponse warehouse;
  WarehouseResponse transfer;
  PartnerResponse partner;
  LocalDateTime createdDate;
  TransactionStatus status;
}