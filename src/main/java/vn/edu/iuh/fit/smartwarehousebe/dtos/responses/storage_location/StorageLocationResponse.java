package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location;

import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageLocationResponse implements Serializable {

  private Long id;
  private String name;
  private Long columnIndex;
  private Long rowIndex;
  private Double maxCapacity;
  private Long warehouseShelfID;
  private List<InventoryResponse> inventories;
}