package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTake.StockTakeResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTake;

@Mapper(
    componentModel = "spring",
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE
)
public interface StockTakeMapper {

  StockTakeMapper INSTANCE = Mappers.getMapper(StockTakeMapper.class);

  @Mapping(target = "code", source = "code")
  StockTakeResponse toDto(StockTake stockTake);

}
