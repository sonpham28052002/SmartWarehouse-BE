package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.ConversionUnit;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionUnitResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private UnitResponse fromUnit;

  private UnitResponse toUnit;

  private Double conversionRate;
}
