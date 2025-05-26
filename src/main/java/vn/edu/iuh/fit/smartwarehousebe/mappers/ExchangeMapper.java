package vn.edu.iuh.fit.smartwarehousebe.mappers;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.Inventory.InventoryResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse.ExchangeDetailWithResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.ExchangeDetail;
import vn.edu.iuh.fit.smartwarehousebe.models.Inventory;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.utils.helpers.Mapperhelper;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {

  ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

  @Mapping(source = "originalTransaction.code", target = "transactionCode")
  @Mapping(source = "stockTake.code", target = "stockTakeCode")
  @Mapping(source = "supplier", target = "supplier")
  @Mapping(source = "creator", target = "creator")
  @Mapping(source = "approver", target = "approver")
  @Mapping(source = "exchangeDetails", target = "exchangeDetails", qualifiedByName = "mapExchangeDetails")
  ExchangeResponse toDto(Exchange exchange);

  @Named("mapExchangeDetails")
  default List<ExchangeDetailWithResponse> mapExchangeDetails(
      List<ExchangeDetail> exchangeDetails) {
    return exchangeDetails.stream().map((i) -> ExchangeDetailWithResponse.builder()
            .damagedProduct(DamagedProductMapper.INSTANCE.toDto(i.getDamagedProduct()))
            .quantity(i.getQuantity())
            .type(i.getType())
            .build())
        .collect(
            Collectors.toList());
  }


}
