package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.StockTake;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateStockTakeRequest {

  private Long warehouseShelfId;
  private List<Long> productIds;
  private String description;
  private Long warehouseId;
}
