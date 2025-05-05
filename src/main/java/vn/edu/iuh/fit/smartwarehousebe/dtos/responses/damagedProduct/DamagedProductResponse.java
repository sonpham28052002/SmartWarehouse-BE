package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse.TransactionDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamageType;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DamagedProductResponse implements Serializable {

  private Long id;
  private long quantity;
  private String description;
  private String transactionCode;
  private String stockTakeCode;
  private boolean isExchange;
  private ExchangeResponse exchange;
  private StockTakeDetailResponse stockTakeDetail;
  private TransactionDetailResponse transactionDetail;
  private DamagedProductStatus status;
  private DamageType type;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
  private boolean deleted;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamagedProductResponse response = (DamagedProductResponse) o;
    return Objects.equals(id, response.id) && type == response.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type);
  }
}
