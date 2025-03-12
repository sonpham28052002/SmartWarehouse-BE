package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StorageLocationResponse implements Serializable {

  private Long id;
  private String name;
  private Long columnIndex;
  private Long rowIndex;
  private Double maxCapacity;
  private Long warehouseShelfID;
  private List<InventoryResponse> inventories;
}