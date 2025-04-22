package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Set;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

@Getter
@Setter
@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String code;

  @Column(name = "transaction_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate;

  @Column(name = "transaction_file")
  private String transactionFile;

  private String description;

  @ManyToOne
  @JoinColumn(name = "executor", nullable = true)
  private User executor;

  @ManyToOne
  @JoinColumn(name = "creator", nullable = false)
  private User creator;

  @ManyToOne
  @JoinColumn(name = "approver", nullable = true)
  private User approver;

  @ManyToOne
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @ManyToOne
  @JoinColumn(name = "transfer_id")
  private Warehouse transfer;

  @ManyToOne
  @JoinColumn(name = "partner_id")
  private Partner partner;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<TransactionDetail> details;

  @Enumerated(EnumType.STRING)
  private TransactionStatus status;

  @PrePersist
  public void setDefault() {
    if (this.status == null) {
      this.status = TransactionStatus.WAITING;
    }
  }
}