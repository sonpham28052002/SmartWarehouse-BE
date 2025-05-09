package vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.enums.StockTakeStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StockTakeResponse implements Serializable {

  private Long id;
  private String description;
  private WarehouseResponse warehouse;
  private StockTakeStatus status;
  private LocalDateTime createdDate;
  private LocalDateTime lastModifiedDate;
  @JsonManagedReference
  private List<StockTakeDetailResponse> stockTakeDetails;
  private boolean deleted;
}
