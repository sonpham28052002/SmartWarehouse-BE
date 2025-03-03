package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.supplier.CreateSupplierRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.supplier.SupplierResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
   Supplier toEntity(SupplierResponse supplierResponse);

   SupplierResponse toDto(Supplier supplier);

   Supplier toEntity(CreateSupplierRequest createSupplierRequest);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Supplier partialUpdate(CreateSupplierRequest createSupplierRequest, @MappingTarget Supplier supplier);
}
