package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.transaction;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GetTransactionDetailRequest implements Serializable {

  private String productName;
  private String productCode;

}
