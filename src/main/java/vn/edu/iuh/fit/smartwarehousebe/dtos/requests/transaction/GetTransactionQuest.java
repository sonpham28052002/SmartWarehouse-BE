package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import java.time.LocalDateTime;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;

/**
 * @description
 * @author: vie
 * @date: 15/3/25
 */
@Value
public class GetTransactionQuest {

  String transactionType;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime endDate;
  Long executor;
  Long warehouse;
  Long transfer;
  Long partner;
  String code;
  TransactionStatus status;
}