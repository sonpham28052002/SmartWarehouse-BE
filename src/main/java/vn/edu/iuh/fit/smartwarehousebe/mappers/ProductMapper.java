package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
   @Mapping(target = "supplierId", source = "supplier.id")
   @Mapping(source = "deleted", target = "deleted")
   @Mapping(source = "createdDate", target = "createdDate")
   @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
   ProductResponse toDto(Product product);

   @Mapping(target = "supplier", ignore = true)
   Product toEntity(CreateProductRequest createProductRequest);

   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   Product partialUpdate(CreateProductRequest createProductRequest, @MappingTarget Product product);
}
