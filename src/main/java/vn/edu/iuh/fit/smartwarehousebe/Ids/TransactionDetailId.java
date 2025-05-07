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
public class TransactionDetailId implements Serializable{
  @Column(name = "transaction_id")
  private Long transactionId;

  @Column(name = "inventory_id")
  private Long inventoryId;
}

