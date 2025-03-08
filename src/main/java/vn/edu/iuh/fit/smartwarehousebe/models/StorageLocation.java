package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "shelf_index")
    private Long shelfIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "storageLocation")
    private List<Inventory> inventories;

}
