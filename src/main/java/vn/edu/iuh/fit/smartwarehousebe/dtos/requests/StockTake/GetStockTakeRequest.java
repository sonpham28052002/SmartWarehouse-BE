package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetStockTakeRequest {

  private String warehouseCode;
  private String code;
  private StockTakeStatus status;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  LocalDateTime endDate;
}
