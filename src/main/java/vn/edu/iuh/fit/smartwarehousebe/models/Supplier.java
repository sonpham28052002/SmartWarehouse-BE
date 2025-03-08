package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import vn.edu.iuh.fit.smartwarehousebe.enums.UserStatus;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE supplier SET deleted = true WHERE id = ?")
public class Supplier extends  Auditable{

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

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<Product> products;

}
