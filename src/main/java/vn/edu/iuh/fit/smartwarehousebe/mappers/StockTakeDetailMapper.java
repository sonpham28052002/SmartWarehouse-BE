package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;

@Mapper(componentModel = "spring")
public interface StockTakeDetailMapper {

  StockTakeDetailMapper INSTANCE = Mappers.getMapper(StockTakeDetailMapper.class);

  @Mapping(source = "inventory", target = "inventory", qualifiedByName = "mapLocation")
  StockTakeDetailResponse toDto(StockTakeDetail stockTakeDetail);

  @Named("mapLocation")
  default InventoryResponse mapLocation(Inventory inventory) {
    if (inventory == null || inventory.getStorageLocation() == null) {
      return null;
    }

    return InventoryResponse.builder()
        .location(inventory.getStorageLocation().getWarehouseShelf().getShelfName() + "-"
            + String.valueOf(
            inventory.getStorageLocation().getColumnIndex()) + "-" +
            String.valueOf(inventory.getStorageLocation().getRowIndex()))
        .id(inventory.getId())
        .product(ProductMapper.INSTANCE.toDto(inventory.getProduct()))
        .unit(UnitMapper.INSTANCE.toDto(inventory.getUnit()))
        .quantity(inventory.getQuantity())
        .status(inventory.getStatus())
        .build();
  }

  StockTakeDetail toEntity(StockTakeDetailResponse response);
}
