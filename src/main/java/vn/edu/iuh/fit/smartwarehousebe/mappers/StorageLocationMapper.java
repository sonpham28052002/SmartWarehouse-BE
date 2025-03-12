package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.storage_location.StorageLocationResponse;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.StorageLocation;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;

@Mapper
public interface StorageLocationMapper {

  StorageLocationMapper INSTANCE = Mappers.getMapper(StorageLocationMapper.class);

  StorageLocation toEntity(StorageLocationResponse supplierResponse);

  StorageLocationResponse toDto(StorageLocation location);

  Supplier toEntity(CreateSupplierRequest createSupplierRequest);
}
