package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponse toDto(User user);

    User toEntity(UserRequest request);
}
