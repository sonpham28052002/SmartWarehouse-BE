package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.responses.user.UserResponse;
import vn.edu.iuh.fit.smartwarehousebe.mappers.UserMapper;
import vn.edu.iuh.fit.smartwarehousebe.models.User;
import vn.edu.iuh.fit.smartwarehousebe.repositories.UserRepository;
import vn.edu.iuh.fit.smartwarehousebe.servies.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/list")
  public ResponseEntity<Page<UserResponse>> index(
      @RequestParam(value = "per_page", defaultValue = "10") int perPage,
      @RequestParam(value = "current_page", defaultValue = "1") int currentPage,
      GetUserQuest request) {
    return ResponseEntity.ok(userService.getUsers(
        PageRequest.of(currentPage - 1, perPage, Sort.by(Sort.Direction.DESC, "id")), request));
  }

  @GetMapping("/UsersManagerNotInWarehouse")
  public ResponseEntity<List<UserResponse>> getUsersManagerNotInWarehouse() {
    return ResponseEntity.ok(
        UserMapper.INSTANCE.toDtoList(userService.getUsersManagerNotInWarehouse()));
  }

  @GetMapping("/getAllUserStaff")
  public ResponseEntity<List<UserResponse>> getAllUserStaff() {
    return ResponseEntity.ok(UserMapper.INSTANCE.toDtoList(userService.getAllUserStaff()));
  }

  @GetMapping("/{id}")
  public UserResponse getUser(@PathVariable Long id) {
    User user = userService.getUserById(id);
    UserResponse response = UserMapper.INSTANCE.toDto(user);
    response.setUserName(user.getUsername());
    return response;
  }

  @PostMapping
  public UserResponse createUser(@RequestBody @Valid UserRequest request) {
    User user = UserMapper.INSTANCE.toEntity(request);
    UserResponse newUser = UserMapper.INSTANCE.toDto(userService.createUser(user));
    newUser.setUserName(user.getUsername());
    return newUser;
  }

  @PutMapping("/{id}")
  public UserResponse updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
    request.setId(id);
    User user = UserMapper.INSTANCE.toEntity(request);
    UserResponse newUser = UserMapper.INSTANCE.toDto(userService.updateUser(user));
    newUser.setUserName(user.getUsername());
    return newUser;
  }

  @DeleteMapping("/{id}")
  public Boolean deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }

  @GetMapping("/{code}/checkCode")
  public ResponseEntity<Boolean> checkCode(@PathVariable String code) {
    return ResponseEntity.ok(userService.checkCodeIsExist(User.class, code));
  }
}
