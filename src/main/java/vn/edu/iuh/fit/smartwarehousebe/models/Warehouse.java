package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String address;

  private String code;
  private String name;

  @OneToMany(mappedBy = "warehouse", fetch = FetchType.EAGER)
  @JsonIgnore
  private Set<User> staffs = new HashSet<>();

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = true)
  @JsonBackReference
  private User manager;

  @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
  private List<WarehouseShelf> warehouseShelves;


}
