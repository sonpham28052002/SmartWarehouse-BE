package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.enums.PartnerType;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "partner")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE partner SET deleted = true WHERE id = ?")
public class Partner extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, length = 10)
  private String code;

  @Column(name = "name", length = 255)
  private String name;

  @Column(name = "phone", length = 10)
  private String phone;

  @Column(name = "email", length = 255)
  private String email;

  private String address;

  @OneToMany(mappedBy = "partner")
  @JsonIgnore
  private List<Product> products;

  @OneToMany(mappedBy = "partner")
  private Set<Transaction> transactions;

  @Enumerated(EnumType.STRING)
  private PartnerType type;
}
