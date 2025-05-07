package vn.edu.iuh.fit.smartwarehousebe.Ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeDetailId implements Serializable {

  @Column(name = "exchange_id")
  private Long exchangeId;
  @Column(name = "damaged_product_id")
  private Long damagedProductId;
}
