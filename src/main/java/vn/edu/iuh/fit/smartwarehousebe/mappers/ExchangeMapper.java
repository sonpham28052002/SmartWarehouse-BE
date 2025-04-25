package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.exchange.ExchangeResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Exchange;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;

@Mapper(componentModel = "spring")
public interface ExchangeMapper {

  ExchangeMapper INSTANCE = Mappers.getMapper(ExchangeMapper.class);

  @Mapping(source = "transaction.code", target = "transactionCode")
  @Mapping(source = "supplier", target = "supplier")
  @Mapping(source = "creator", target = "creator")
  @Mapping(source = "approver", target = "approver")
  ExchangeResponse toDto(Exchange exchange);


}
