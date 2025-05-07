package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.utils.helpers.Mapperhelper;

@Mapper(componentModel = "spring")
public interface DamagedProductMapper {

  DamagedProductMapper INSTANCE = Mappers.getMapper(DamagedProductMapper.class);

  @Mapping(source = "stockTakeDetail.inventory", target = "stockTakeDetail.inventory", qualifiedByName = "mapLocation")
  @Mapping(source = "transactionDetail.inventory", target = "transactionDetail.inventory", qualifiedByName = "mapLocation")
  @Mapping(source = "stockTakeDetail.stockTake.code", target = "stockTakeCode")
  @Mapping(source = "transactionDetail.transaction.code", target = "transactionCode")
  @Mapping(source = "type", target = "type")
  @Mapping(source = "exchangeType", target = "exchangeType")
  DamagedProductResponse toDto(DamagedProduct damagedProduct);

  @Named("mapLocation")
  default InventoryResponse mapLocation(Inventory inventory) {
    return new Mapperhelper().mapInventory(inventory);
  }

}
