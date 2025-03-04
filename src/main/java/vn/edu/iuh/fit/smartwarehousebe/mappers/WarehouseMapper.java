package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;


@Mapper
public interface WarehouseMapper {

   WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);


   Warehouse toEntity(CreateWarehouseRequest createWarehouseRequest);

   @Mapping(source = "deleted", target = "deleted")
   WarehouseResponse toDto(Warehouse warehouse);
}
