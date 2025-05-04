package vn.edu.iuh.fit.smartwarehousebe.utils.helpers;

import java.util.HashSet;
import java.util.Set;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.damagedProduct.DamagedProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.DamagedProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.ProductMapper;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UnitMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
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

  public Set<DamagedProductWithResponse> mapDamagedProductResponses(Set<DamagedProduct> damagedProducts) {
    if (damagedProducts == null || damagedProducts == null) {
      return null;
    }
    Set<StockTakeDetailResponse.DamagedProductWithResponse> result = new HashSet<>();
    for (DamagedProduct damagedProduct : damagedProducts) {
      result.add(StockTakeDetailResponse.DamagedProductWithResponse.builder()
          .id(damagedProduct.getId())
          .stockTakeCode(damagedProduct.getStockTakeDetail() != null ? damagedProduct.getStockTakeDetail().getStockTake().getCode() : null)
          .transactionCode(damagedProduct.getTransactionDetail()  != null ? damagedProduct.getTransactionDetail().getTransaction().getCode() : null)
          .quantity(damagedProduct.getQuantity())
          .status(damagedProduct.getStatus())
          .description(damagedProduct.getDescription())
          .isExchange(damagedProduct.isExchange())
          .build());
    }
    return result;
  }


}
