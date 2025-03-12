package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.WarehouseShelf;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WarehouseShelfRequest {

  private Long id;
  private Long columnNum;
  private Long rowNum;
  private String shelfName;
  private String description;
  private Double maxCapacity;
  private Long warehouseId;
}
