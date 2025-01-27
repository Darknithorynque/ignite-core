package com.example.ignite_core.User;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/byEmail")
    public Optional<UserEntity> getUserByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

    @PutMapping("/update/{id}")
    public UserEntity updateUser(@RequestBody UpdateUserRequest user, @PathVariable Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/allergies/create/{id}")
    public void addAllergies(@RequestBody ArrayList<String> allergies, @PathVariable Long id) {
        userService.addAllergies(allergies,id);
    }

    @PutMapping("/allergies/delete/{id}")
    public void deleteAllergies(@RequestBody ArrayList<String> allergies,@PathVariable Long id) {
        userService.deleteAllergies(allergies,id);
    }

    @PutMapping("/allergies/update/{id}")
    public void updateAllergies(@RequestBody ArrayList<String> allergies,@PathVariable Long id) {
        userService.updateAllergies(allergies,id);
    }
}
