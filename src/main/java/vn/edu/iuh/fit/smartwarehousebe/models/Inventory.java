package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import vn.edu.iuh.fit.smartwarehousebe.enums.InventoryStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionStatus;
import vn.edu.iuh.fit.smartwarehousebe.enums.TransactionType;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE inventory SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Inventory extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "storage_location_id")
  private StorageLocation storageLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id")
  private Product product;

  private Long inventoryQuantity;

  @ManyToOne()
  private Unit unit;

  @OneToMany(mappedBy = "inventory", orphanRemoval = true)
  @JsonManagedReference
  private List<StockTakeDetail> stockTakeDetails;

  @OneToMany(mappedBy = "inventory", orphanRemoval = true)
  @JsonManagedReference
  private List<TransactionDetail> transactionDetails;

  @Enumerated(EnumType.STRING)
  private InventoryStatus status;

  @Transient
  private Long quantity;

  @PrePersist
  public void setDefault() {
    if (this.status == null) {
      this.status = InventoryStatus.INACTIVE;
    }
  }

  @PostLoad
  public void calculateQuantity() {
    quantity = this.inventoryQuantity;

    if (this.transactionDetails != null && !this.transactionDetails.isEmpty()) {
      LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
      LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

      for (TransactionDetail detail : this.transactionDetails) {
        if (detail.getTransaction() != null
            && detail.getTransaction().getStatus() == TransactionStatus.COMPLETE
            && detail.getLastModifiedDate() != null) {

          LocalDateTime modified = detail.getLastModifiedDate();

          if (!modified.isBefore(startOfDay) && !modified.isAfter(endOfDay)) {
            TransactionType type = detail.getTransactionType();
            if (type == TransactionType.IMPORT_FROM_SUPPLIER || type == TransactionType.IMPORT_FROM_WAREHOUSE) {
              quantity += detail.getQuantity();
            } else {
              quantity -= detail.getQuantity();
            }
          }
        }
      }
    }
  }

}

