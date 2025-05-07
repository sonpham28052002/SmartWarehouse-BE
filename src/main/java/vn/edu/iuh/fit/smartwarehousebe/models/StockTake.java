package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeStatus;

@Entity
@Table(name = "stock_take")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE stock_take SET deleted = true, status = 4 WHERE id = ?")
public class StockTake extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "warehouse_id")
  @JsonIgnore
  private Warehouse warehouse;

  @OneToMany(mappedBy = "stockTake", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  private List<StockTakeDetail> stockTakeDetails;

  private String description;

  @Enumerated(EnumType.ORDINAL)
  private StockTakeStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "executor_id")
  @JsonIgnore
  private User executor;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "creator_id")
  @JsonIgnore
  private User creator;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "approver_id")
  @JsonIgnore
  private User approver;

  @OneToMany(mappedBy = "stockTake")
  private List<Exchange> exchanges;

  @Override
  public String toString() {
    return "StockTake{" + "id=" + id + ", stockTakeDetails=" + stockTakeDetails + ", description='"
        + description + '\'' + ", status=" + status + '}';
  }
}
