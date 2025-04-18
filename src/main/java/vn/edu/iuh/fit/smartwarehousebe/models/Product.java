package vn.edu.iuh.fit.smartwarehousebe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.regex.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
public class Product extends Auditable implements Serializable {

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

  @ManyToOne
  private Unit unit;

  @Column(name = "image", length = 255)
  private String image;

  private Double unitWeight;

  @ManyToOne
  @JoinColumn(name = "partner_id")
  @JsonIgnore
  private Partner partner;

  @OneToMany(mappedBy = "product")
  private List<Inventory> inventories;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
  @JsonIgnore
  private List<ConversionUnit> conversionUnits;


  @PrePersist
  public void generateSku() {
    if (sku == null || sku.isEmpty()) {
      String productCode = code != null ? code.toUpperCase() : "NO_CODE";
      String productName =
          name != null ? removeVietnameseAccents(name).replaceAll("\\s+", "").toUpperCase()
              : "NO_NAME";

      String partnerCode =
          partner != null && partner.getCode() != null ? partner.getCode().toString()
              : "NO_CODE";
      String partnerName =
          partner != null && partner.getName() != null ? removeVietnameseAccents(
              partner.getName()).replaceAll("\\s+", "").toUpperCase() : "NO_SUPP";

      String unitCode = unit != null && unit.getCode() != null ? unit.getCode() : "NO_CODE";
      String unitName = unit != null && unit.getName() != null ? removeVietnameseAccents(
          unit.getName()).replaceAll("\\s+", "").toUpperCase() : "NO_UNIT";

      String weight = unitWeight != null ? String.format("%.2f", unitWeight) : "0";

      this.sku = String.format("%s-%s-%s-%s-%s-%s-%s",
          productCode, productName, partnerCode, partnerName, unitCode, unitName, weight);
    }
  }

  /**
   * Loại bỏ dấu tiếng Việt khỏi chuỗi.
   */
  public static String removeVietnameseAccents(String str) {
    str = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(str).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
  }


}
