package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.product.CreateProductRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.product.ProductResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  Product toEntity(ProductResponse productResponse);

  @Mapping(target = "partnerId", source = "partner.id")
  @Mapping(source = "deleted", target = "deleted")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  @Mapping(source = "unit", target = "unit")
  @Mapping(source = "unitWeight", target = "unitWeight")
  @Mapping(source = "partner", target = "partner")
  ProductResponse toDto(Product product);

  @Mapping(target = "partner", ignore = true)
  Product toEntity(CreateProductRequest createProductRequest);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Product partialUpdate(CreateProductRequest createProductRequest, @MappingTarget Product product);

}
