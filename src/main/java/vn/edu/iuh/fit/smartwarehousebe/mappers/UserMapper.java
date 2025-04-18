package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserImportRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    UserResponse toDto(User user);

    User toEntity(UserRequest request);

    User toEntity(UserImportRequest request);

    @Mapping(source = "username", target = "userName")
    @Mapping(source = "deleted", target = "deleted")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    List<UserResponse> toDtoList(List<User> users);

    List<User> toEntityList(List<UserRequest> userRequests);
}
