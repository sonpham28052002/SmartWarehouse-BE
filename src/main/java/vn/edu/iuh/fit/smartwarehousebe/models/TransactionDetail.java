package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import lombok.*;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

@Getter
@Setter
@Entity
@Table(name = "transaction_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetail extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "inventory_id", nullable = false)
  private Inventory inventory;

  private int quantity;

  @Column(name = "transaction_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  @ManyToOne
  @JoinColumn(name = "transaction_id", nullable = false)
  private Transaction transaction;

  @OneToMany(mappedBy = "transactionDetail", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private Set<DamagedProduct> damagedProducts;
}