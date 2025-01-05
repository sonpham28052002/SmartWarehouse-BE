package vn.edu.iuh.fit.smartwarehousebe.mappers;

import org.mapstruct.Mapper;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.auth.AuthResponse;
import vn.edu.iuh.fit.smartwarehousebe.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AuthResponse toDto(User user);

    User toEntity(AuthResponse response);
}