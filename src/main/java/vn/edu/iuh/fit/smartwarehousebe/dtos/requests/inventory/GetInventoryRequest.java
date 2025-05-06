package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetInventoryRequest {
  private String productName;
  private String productCode;
  private String warehouseName;
  private String warehouseCode;
}
