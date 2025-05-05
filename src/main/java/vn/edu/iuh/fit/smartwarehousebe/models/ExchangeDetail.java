package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.Ids.ExchangeDetailId;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;

@Entity
@Table(name = "exchange_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE exchange_detail SET deleted = true WHERE id = ?")
public class ExchangeDetail extends Auditable{

  @EmbeddedId
  private ExchangeDetailId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("exchangeId")
  @JoinColumn(name = "exchange_id")
  @JsonIgnore
  private Exchange exchange;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("damagedProductId")
  @JoinColumn(name = "damaged_product_id")
  private DamagedProduct damagedProduct;

  private int quantity;

  @Enumerated(EnumType.STRING)
  private ExchangeType type;

}
