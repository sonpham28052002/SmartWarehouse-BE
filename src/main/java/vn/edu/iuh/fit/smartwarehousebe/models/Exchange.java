package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;

@Entity
@Table(name = "exchange")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE exchange SET deleted = true WHERE id = ?")
public class Exchange extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  @OneToMany(mappedBy = "exchange", fetch = FetchType.EAGER)
  private Set<DamagedProduct> damagedProducts;

  @Enumerated(EnumType.STRING)
  private ExchangeType type;

  @ManyToOne
  @JoinColumn(name = "original_transaction_id")
  private Transaction originalTransaction;

  @OneToMany(mappedBy = "exchange")
  private List<ExchangeDetail> exchangeDetails;

  @ManyToOne
  @JoinColumn(name = "stock_take_id")
  private StockTake stockTake;

  @ManyToOne
  private Partner supplier;

  @ManyToOne
  private User approver;

  @ManyToOne
  private User creator;


}
