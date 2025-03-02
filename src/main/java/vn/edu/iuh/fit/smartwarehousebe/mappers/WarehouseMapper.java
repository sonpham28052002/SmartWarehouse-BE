package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.CreateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.warehouse.UpdateWarehouseRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.warehouse.WarehouseResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Warehouse;

/**
 * @description
 * @author: vie
 * @date: 1/3/25
 */
@Mapper(componentModel = "spring")
public interface WarehouseMapper {
   @Mapping(target = "createdDate", expression = "java(java.time.LocalDateTime.now())")
   Warehouse toEntity(CreateWarehouseRequest createWarehouseRequest);

   @Mapping(target = "lastModifiedDate", expression = "java(java.time.LocalDateTime.now())")
   Warehouse toEntity(UpdateWarehouseRequest updateWarehouseRequest);

   @Mapping(target = "manager", source = "manager.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
   WarehouseResponse toDto(Warehouse warehouse);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Warehouse partialUpdate(CreateWarehouseRequest createWarehouseRequest, @MappingTarget Warehouse warehouse);


   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Warehouse partialUpdate(UpdateWarehouseRequest updateWarehouseRequest, @MappingTarget Warehouse warehouse);
}
