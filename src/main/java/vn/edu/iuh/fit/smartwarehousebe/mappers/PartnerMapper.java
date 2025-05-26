package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.CreatePartnerRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.partner.PartnerResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

  Partner toEntity(PartnerResponse partnerResponse);

  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  @Mapping(source = "type", target = "type")
  PartnerResponse toDto(Partner partner);

  Partner toEntity(CreatePartnerRequest createPartnerRequest);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Partner partialUpdate(CreatePartnerRequest createPartnerRequest,
      @MappingTarget Partner partner);
}
