package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "storage_location")
@SQLDelete(sql = "UPDATE storage_location SET deleted = true WHERE id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StorageLocation extends Auditable implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "column_index")
  private Long columnIndex;

  @Column(name = "row_index")
  private Long rowIndex;

  @Column(name = "max_capacity", nullable = false)
  private Double maxCapacity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_shelf_id")
  private WarehouseShelf warehouseShelf;

  @OneToMany(mappedBy = "storageLocation")
  private List<Inventory> inventories;

}
