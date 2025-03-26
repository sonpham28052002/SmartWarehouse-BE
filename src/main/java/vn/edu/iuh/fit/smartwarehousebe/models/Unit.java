package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Table(name = "unit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE unit SET deleted = true WHERE id = ?")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Unit extends Auditable implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String code;

  private String name;

  @OneToMany(mappedBy = "unit")
  @JsonIgnore
  private List<Product> product;

  @OneToMany(mappedBy = "unit")
  @JsonIgnore
  private List<Inventory> inventories;
}
