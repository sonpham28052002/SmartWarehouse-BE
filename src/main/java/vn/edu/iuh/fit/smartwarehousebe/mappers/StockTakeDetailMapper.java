package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.StockTakeDetail.StockTakeDetailResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.StockTakeDetail;

@Mapper(componentModel = "spring")
public interface StockTakeDetailMapper {

  StockTakeDetailMapper INSTANCE = Mappers.getMapper(StockTakeDetailMapper.class);

  StockTakeDetailResponse toDto(StockTakeDetail stockTakeDetail);

  StockTakeDetail toEntity(StockTakeDetailResponse response);
}
