package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.WarehouseShelf.WarehouseShelfResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;
import vn.edu.iuh.fit.smartwarehousebe.models.WarehouseShelf;

@Mapper(componentModel = "spring")
public interface WarehouseShelfMapper {

  WarehouseShelfMapper INSTANCE = Mappers.getMapper(WarehouseShelfMapper.class);

  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  @Mapping(source = "warehouse.id", target = "warehouseId")
  WarehouseShelfResponse toDto(WarehouseShelf warehouse);

  Warehouse toEntity(WarehouseShelfResponse response);

}
