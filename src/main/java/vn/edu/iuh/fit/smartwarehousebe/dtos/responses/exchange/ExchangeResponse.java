package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.ExchangeType;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExchangeResponse implements Serializable {

  private Long id;

  private String code;

  private ExchangeType type;

  private String transactionCode;
  private String stockTakeCode;

  private PartnerResponse supplier;
  private UserResponse approver;
  private UserResponse creator;
  private List<ExchangeDetailWithResponse> exchangeDetails;

  @Value
  @Builder
  public static class ExchangeDetailWithResponse implements Serializable {
    private DamagedProductResponse damagedProduct;
    private int quantity;
    private ExchangeType type;
  }


}
