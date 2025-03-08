package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name = "conversion_unit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@SQLDelete(sql = "UPDATE conversion_unit SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class ConversionUnit extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Unit fromUnit;

    @ManyToOne
    private Unit toUnit;

    @ManyToOne
    @JsonIgnore
    private Product product;

    @Column(nullable = false)
    private Double conversionRate;

    @OneToMany(mappedBy = "unit")
    private List<Inventory> inventories;

}
