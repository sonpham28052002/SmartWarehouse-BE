package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import vn.edu.iuh.fit.smartwarehousebe.Ids.ExchangeDetailId;

@Entity
public class ExchangeDetail {

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

}
