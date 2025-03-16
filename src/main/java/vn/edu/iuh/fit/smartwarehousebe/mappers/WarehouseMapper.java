package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;


@Mapper(componentModel = "spring")
public interface WarehouseMapper {

  WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);

  Warehouse toEntity(CreateWarehouseRequest createWarehouseRequest);

  Warehouse toEntity(WarehouseResponse warehouseResponse);

  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  WarehouseResponse toDto(Warehouse warehouse);

  @Mapping(target = "shelfNum", ignore = true)
  @Mapping(target = "rowNum", ignore = true)
  @Mapping(target = "columnNum", ignore = true)
  @Mapping(target = "staffs", ignore = true)
  @Mapping(target = "manager", ignore = true)
  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  WarehouseResponse toDtoV2(Warehouse warehouse);
}
