package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
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
  private Inventory inventory;

  @ManyToOne(fetch = FetchType.EAGER)
  private StockTake stockTake;

  @ManyToOne(fetch = FetchType.EAGER)
  private Transaction transaction;

  @Enumerated(EnumType.STRING)
  private DamagedProductStatus status;

  @PrePersist
  public void prePersist() {
    this.isExchange = this.exchange != null && this.exchange.getId() != null;
  }


}
