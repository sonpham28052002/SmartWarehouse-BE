package vn.edu.iuh.fit.smartwarehousebe.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
public class Product extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name", length = 255)
    private String name;

    private String description;

    @Column(name = "image", length = 255)
    private String image;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

}
