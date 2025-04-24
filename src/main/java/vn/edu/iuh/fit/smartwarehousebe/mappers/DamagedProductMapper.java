package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;

@Mapper(componentModel = "spring")
public interface DamagedProductMapper {

  DamagedProductMapper INSTANCE = Mappers.getMapper(DamagedProductMapper.class);

  @Mapping(source = "stockTake.code", target = "stockTakeCode")
  @Mapping(source = "inventory.storageLocation.name", target = "inventory.storageLocationName")
  DamagedProductResponse toDto(DamagedProduct damagedProduct);

}
