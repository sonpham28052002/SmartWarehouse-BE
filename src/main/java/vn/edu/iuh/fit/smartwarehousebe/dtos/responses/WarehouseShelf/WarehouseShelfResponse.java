package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.WarehouseShelf;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WarehouseShelfResponse implements Serializable {

  private Long id;
  private Long columnNum;
  private Long rowNum;
  private String shelfName;
  private String description;
  private Double maxCapacity;
  private boolean deleted;
  private LocalDateTime lastModifiedDate;
  private LocalDateTime createdDate;
  private Long warehouseId;

}
