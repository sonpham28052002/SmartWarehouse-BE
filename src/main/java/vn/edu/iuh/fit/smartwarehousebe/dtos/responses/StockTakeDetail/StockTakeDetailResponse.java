package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeDetailStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StockTakeDetailResponse implements Serializable {

  @JsonManagedReference
  private InventoryResponse inventory;
  private Long expectedQuantity;
  private Long actualQuantity;
  private Long damagedQuantity;
  private String description;
  private StockTakeDetailStatus status;
}
