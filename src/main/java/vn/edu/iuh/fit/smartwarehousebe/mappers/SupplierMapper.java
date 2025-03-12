package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

  Supplier toEntity(SupplierResponse supplierResponse);

  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  SupplierResponse toDto(Supplier supplier);

  Supplier toEntity(CreateSupplierRequest createSupplierRequest);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Supplier partialUpdate(CreateSupplierRequest createSupplierRequest,
      @MappingTarget Supplier supplier);
}
