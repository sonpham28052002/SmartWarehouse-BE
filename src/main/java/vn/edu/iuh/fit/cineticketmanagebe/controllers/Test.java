package vn.edu.iuh.fit.cineticketmanagebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.cineticketmanagebe.models.User;
import vn.edu.iuh.fit.cineticketmanagebe.servies.UserService;

@RestController
@RequestMapping()
public class Test {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/login")
    public User adminString(@AuthenticationPrincipal User user){
        return userService.loadUserByUsername(user.getUsername());
    }

    @GetMapping("/user/login")
    public User userString(@AuthenticationPrincipal User user){ return user; }
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
