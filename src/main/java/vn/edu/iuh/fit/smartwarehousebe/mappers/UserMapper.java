package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "deleted", target = "deleted")
    UserResponse toDto(User user);

    User toEntity(UserRequest request);

    @Mapping(source = "username", target = "userName")
    @Mapping(source = "deleted", target = "deleted")
    List<UserResponse> toDtoList(List<User> users);

    List<User> toEntityList(List<UserRequest> userRequests);
}
