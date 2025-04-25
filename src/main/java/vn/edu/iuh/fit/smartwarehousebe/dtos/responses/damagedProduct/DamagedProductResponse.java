package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.DamagedProductStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DamagedProductResponse implements Serializable {

  private Long id;
  private Long quantity;
  private String description;
  private boolean isExchange;
  @JsonBackReference
  private ExchangeResponse exchange;
  @JsonBackReference
  private InventoryResponse inventory;
  private String stockTakeCode;
  @JsonBackReference
  private TransactionResponse transaction;
  private DamagedProductStatus status;

}
