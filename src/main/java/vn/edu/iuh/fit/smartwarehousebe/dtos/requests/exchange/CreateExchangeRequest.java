package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.exchange;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateExchangeRequest {
  private String transactionCode;
  private String stockTakeCode;
  private String note;
  private List<DamagedProductResponse> damagedProducts;
}
