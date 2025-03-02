package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "warehouse")
@SQLDelete(sql = "UPDATE warehouse SET deleted = true WHERE id = ?")
public class Warehouse extends Auditable {
   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;

   private String address;

   private String code;

   private String name;

   @OneToMany(mappedBy = "warehouse")
   private Set<User> staffs;

   @OneToOne
   @JoinColumn(name = "manager", referencedColumnName = "id")
   private User manager;

}