package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExchangeResponse implements Serializable {

  private Long id;

  private String code;

  @JsonIgnoreProperties({"inventory", "stockTake", "transaction", "exchange"})
  private List<DamagedProductResponse> damagedProducts;

  private ExchangeType type;

  private TransactionResponse transaction;
}
