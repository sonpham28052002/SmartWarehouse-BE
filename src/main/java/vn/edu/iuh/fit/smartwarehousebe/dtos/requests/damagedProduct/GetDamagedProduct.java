package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.damagedProduct;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

@Value
public class GetDamagedProduct {

  String productCode;
  String productName;
  String inventoryName;
  String stockTakeCode;
  String transactionCode;
  String supplierCode;
  String supplierName;
  String exchangeStatus;
  String exchangeType;
}
