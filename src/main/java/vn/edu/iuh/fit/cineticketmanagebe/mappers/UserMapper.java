package vn.edu.iuh.fit.cineticketmanagebe.mappers;

import org.mapstruct.Mapper;
import vn.edu.iuh.fit.cineticketmanagebe.dtos.responses.auth.AuthResponse;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AuthResponse toDto(User user);

    User toEntity(AuthResponse response);
}