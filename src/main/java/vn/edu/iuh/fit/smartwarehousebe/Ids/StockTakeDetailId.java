package vn.edu.iuh.fit.smartwarehousebe.Ids;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTakeDetailId implements Serializable {

  @Column(name = "stock_take_id")
  private Long stockTakeId;

  @Column(name = "inventory_id")
  private Long inventoryId;
}
