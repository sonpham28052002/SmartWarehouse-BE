package vn.edu.iuh.fit.cineticketmanagebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;
import vn.edu.iuh.fit.cineticketmanagebe.repositories.UserRepository;
import vn.edu.iuh.fit.cineticketmanagebe.servies.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public Page<User> index(@AuthenticationPrincipal User user){
        return userRepository.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
