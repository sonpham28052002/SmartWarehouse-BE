package vn.edu.iuh.fit.smartwarehousebe.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.UserRequest;
import vn.edu.iuh.fit.smartwarehousebe.dtos.requests.user.GetUserQuest;
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
    public ResponseEntity<Page<User>> index(@RequestParam(value = "per_page", defaultValue = "10") int perPage, @RequestParam(value = "current_page", defaultValue = "1") int currentPage, GetUserQuest request) {
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(currentPage - 1, perPage, Sort.by(Sort.Direction.DESC, "id")), request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.INSTANCE.toDto(userService.getUserById(id)));
    }
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        User user = UserMapper.INSTANCE.toEntity(request);
        return ResponseEntity.ok(UserMapper.INSTANCE.toDto(userService.createUser(user)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        request.setId(id);
        User user = UserMapper.INSTANCE.toEntity(request);
        return ResponseEntity.ok(UserMapper.INSTANCE.toDto(userService.updateUser(user)));
    }

    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return true;
        } catch (Exception exception){
            return false;
        }
    }
}
