package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Entity
@Table(name = "warehouse")
@SQLDelete(sql = "UPDATE warehouse SET deleted = true, manager_id = null WHERE id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Warehouse extends Auditable implements Serializable {
   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String address;

   private String code;

   private String name;

   @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
   @JsonIgnoreProperties({"authorities", "warehouseManager", "warehouse"})
   private Set<User> staffs = new HashSet<>();

   @OneToOne
   @JoinColumn(name = "manager_id", referencedColumnName = "id")
   @JsonIgnoreProperties({"authorities", "warehouseManager", "warehouse"})
   private User manager;

   @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
   private List<WarehouseShelf> warehouseShelves;
}