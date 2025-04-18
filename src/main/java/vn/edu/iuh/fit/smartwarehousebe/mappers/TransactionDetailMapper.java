package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse.TransactionDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;

@Mapper(componentModel = "spring")
public interface TransactionDetailMapper {

  TransactionDetailMapper INSTANCE = Mappers.getMapper(TransactionDetailMapper.class);

  @Mapping(source = "inventory", target = "inventory", qualifiedByName = "mapLocation")
  TransactionDetailResponse toDto(TransactionDetail detail);

  @Named("mapLocation")
  default InventoryResponse mapLocation(Inventory inventory) {
    if (inventory == null) {
      return null;
    }
    String location = "";
    if (inventory.getStorageLocation() != null) {
      location = inventory.getStorageLocation().getWarehouseShelf().getShelfName() + "-"
          + String.valueOf(
          inventory.getStorageLocation().getColumnIndex()) + "-" +
          String.valueOf(inventory.getStorageLocation().getRowIndex());
    }
    return InventoryResponse.builder()
        .location(location)
        .id(inventory.getId())
        .product(ProductMapper.INSTANCE.toDto(inventory.getProduct()))
        .unit(UnitMapper.INSTANCE.toDto(inventory.getUnit()))
        .quantity(inventory.getQuantity())
        .build();
  }
}
