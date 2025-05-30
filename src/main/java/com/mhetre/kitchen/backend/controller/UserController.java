package com.mhetre.kitchen.backend.controller;

import com.mhetre.kitchen.backend.model.User;
import com.mhetre.kitchen.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/hello")
    public String sayHello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from Mhetre's Kitchen backend!");
        return response.toString();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
