package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamageType;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeDetailStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StockTakeDetailResponse implements Serializable {

  private InventoryResponse inventory;
  private Long expectedQuantity;
  private Long actualQuantity;
  private Long damagedQuantity;
  private String description;
  private StockTakeDetailStatus status;
  private Set<DamagedProductWithResponse> damagedProducts;

  @Value
  @Builder
  public static class DamagedProductWithResponse implements Serializable {

    Long id;
    Long quantity;
    String description;
    String transactionCode;
    String stockTakeCode;
    DamagedProductStatus status;
    DamageType type;
    LocalDateTime createdDate;
    LocalDateTime lastModifiedDate;
    boolean deleted;
  }
}
