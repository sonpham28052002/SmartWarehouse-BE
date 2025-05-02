package vn.edu.iuh.fit.smartwarehousebe.utils.helpers;

import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;

public class Mapperhelper {

  public InventoryResponse mapInventory(Inventory inventory) {
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


}
