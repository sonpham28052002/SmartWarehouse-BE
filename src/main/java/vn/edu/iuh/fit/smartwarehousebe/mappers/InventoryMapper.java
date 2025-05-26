package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

  InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

  @Mapping(source = "storageLocation.name", target = "location")
  @Mapping(source = "storageLocation.name", target = "storageLocationName")
  @Mapping(source = "storageLocation.warehouseShelf.warehouse.code", target = "wareHouseCode")
  @Mapping(source = "storageLocation.warehouseShelf.warehouse.name", target = "wareHouseName")
  InventoryResponse toDto(Inventory inventory);

}
