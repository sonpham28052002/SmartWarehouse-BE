package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE inventory SET deleted = true WHERE id = ?")
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

  private Long quantity;

  @ManyToOne()
  private Unit unit;

  @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<StockTakeDetail> stockTakeDetails;
}

