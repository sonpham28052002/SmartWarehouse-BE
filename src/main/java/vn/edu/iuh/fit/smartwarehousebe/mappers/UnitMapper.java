package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.unit.UnitRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.unit.UnitResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.Unit;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

    Unit toEntity(UnitResponse unitResponse);

    Unit toEntity(UnitRequest unitRequest);

    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    UnitResponse toDto(Unit unit);

    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    List<UnitResponse> toDtoList(List<Unit> units);
}
