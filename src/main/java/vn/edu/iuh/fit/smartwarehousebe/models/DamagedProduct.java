package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamageType;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;

@Entity
@Table(name = "damaged_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE damaged_product SET deleted = true WHERE id = ?")
public class DamagedProduct extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long quantity;

  private String description;

  private boolean isExchange;

  @ManyToOne(fetch = FetchType.EAGER)
  private Exchange exchange;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumns({
      @JoinColumn(name = "stock_take_id", referencedColumnName = "stock_take_id"),
      @JoinColumn(name = "stock_take_inventory_id", referencedColumnName = "inventory_id")
  })
  private StockTakeDetail stockTakeDetail;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumns({
      @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id"),
      @JoinColumn(name = "transaction_inventory_id", referencedColumnName = "inventory_id")
  })
  private TransactionDetail transactionDetail;

  @Enumerated(EnumType.STRING)
  private DamagedProductStatus status;

  @Enumerated(EnumType.STRING)
  private DamageType type;

  @PrePersist
  public void prePersist() {
    this.isExchange = this.exchange != null && this.exchange.getId() != null;
  }
}
