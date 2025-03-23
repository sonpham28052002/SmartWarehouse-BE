package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake;

import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetStockTakeRequest {

  private String warehouseCode;
  private StockTakeStatus status;
}
