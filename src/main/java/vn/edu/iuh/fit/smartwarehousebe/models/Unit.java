package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Unit extends Auditable {

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

  @OneToMany(mappedBy = "unit")
  @JsonIgnore
  private List<TransactionDetail> transactionDetails;
}
