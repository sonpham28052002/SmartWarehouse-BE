package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.damagedProduct;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetDamagedProduct {

  private String productCode;
  private String productName;
  private String inventoryName;
  private String stockTakeCode;
  private String transactionCode;
}
