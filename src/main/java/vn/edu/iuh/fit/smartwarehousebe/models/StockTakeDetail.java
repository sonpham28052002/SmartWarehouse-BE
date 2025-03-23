package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vn.edu.iuh.fit.smartwarehousebe.Ids.StockTakeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeDetailStatus;

@Entity
@Table(name = "stock_take_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE stock_take_detail SET deleted = true, status = 5 WHERE inventory_id = ? AND stock_take_id = ?  ")
@JsonIgnoreProperties({"stockTake", "inventory"})
public class StockTakeDetail extends Auditable implements Serializable {

  @EmbeddedId
  private StockTakeDetailId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("stockTakeId")
  @JoinColumn(name = "stock_take_id")
  @JsonIgnore
  private StockTake stockTake;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("inventoryId")
  @JoinColumn(name = "inventory_id")
  private Inventory inventory;

  private Long expectedQuantity;
  private Long actualQuantity;
  private Long damagedQuantity;
  private String description;

  @Enumerated(EnumType.ORDINAL)
  private StockTakeDetailStatus status;
}

