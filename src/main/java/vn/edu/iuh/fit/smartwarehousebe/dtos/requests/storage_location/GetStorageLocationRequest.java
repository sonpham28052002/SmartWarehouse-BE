package vn.edu.iuh.fit.smartwarehousebe.dtos.requests.storage_location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class GetStorageLocationRequest {

  private String name;
  private Long shelfId;
}
