package vn.edu.iuh.fit.smartwarehousebe.mappers;

import java.util.HashSet;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse.DamagedProductWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.transaction.TransactionWithDetailResponse.TransactionDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.DamagedProduct;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.TransactionDetail;
import vn.edu.iuh.fit.smartwarehousebe.utils.helpers.Mapperhelper;

@Mapper(componentModel = "spring")
public interface TransactionDetailMapper {

  TransactionDetailMapper INSTANCE = Mappers.getMapper(TransactionDetailMapper.class);

  @Mapping(source = "inventory", target = "inventory", qualifiedByName = "mapLocation")
  @Mapping(target = "damagedProducts", source = "damagedProducts", qualifiedByName = "mapDamagedProducts")
  TransactionDetailResponse toDto(TransactionDetail detail);

  @Named("mapLocation")
  default InventoryResponse mapLocation(Inventory inventory) {
    return new Mapperhelper().mapInventory(inventory);
  }

  @Named("mapDamagedProducts")
  default Set<DamagedProductWithResponse> mapDamagedProducts(Set<DamagedProduct> damagedProducts) {
    return new Mapperhelper().mapDamagedProductResponses(damagedProducts);
  }
}
