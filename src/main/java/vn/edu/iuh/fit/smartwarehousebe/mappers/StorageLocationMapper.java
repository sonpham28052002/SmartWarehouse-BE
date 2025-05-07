package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.partner.CreatePartnerRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location.StorageLocationResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Partner;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;

@Mapper(componentModel = "spring")
public interface StorageLocationMapper {

  StorageLocationMapper INSTANCE = Mappers.getMapper(StorageLocationMapper.class);


  StorageLocation toEntity(StorageLocationResponse storageLocationResponse);

  StorageLocationResponse toDto(StorageLocation location);

  Partner toEntity(CreatePartnerRequest createPartnerRequest);
}
