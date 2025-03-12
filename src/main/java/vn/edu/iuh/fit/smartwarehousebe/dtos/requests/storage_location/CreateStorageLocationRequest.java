package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CreateStorageLocationRequest {

  private Long id;
  private Long columnIndex;
  private Long rowIndex;
  private String name;
  private Double maxCapacity;
  private InventoryResponse inventoryResponses;
}
