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

  private List<Long> productIds;
  private List<Long> shelfIds;
  private String description;
  private Long warehouseId;
}
