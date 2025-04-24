package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InventoryResponse implements Serializable {

  private Long id;
  private ProductResponse product;
  private Long quantity;
  private UnitResponse unit;
  private String location;
  private InventoryStatus status;
  private String storageLocationName;
}
