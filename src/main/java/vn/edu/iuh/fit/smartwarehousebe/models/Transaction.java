package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "transaction_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate;

  @Column(name = "transaction_file")
  private String transactionFile;

  private String description;

  @ManyToOne
  @JoinColumn(name = "executor", nullable = false)
  private User executor;

  @ManyToOne
  @JoinColumn(name = "warehouse_id")
  private Warehouse warehouse;

  @ManyToOne
  @JoinColumn(name = "transfer_id")
  private Warehouse transfer;

  @ManyToOne
  @JoinColumn(name = "supplier_id")
  private Supplier supplier;

  @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<TransactionDetail> details;
}