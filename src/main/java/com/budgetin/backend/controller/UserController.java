package com.budgetin.backend.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    // class untuk menyimpan data user sementara
    public static class User {
        public String name;
        public String email;
        public String password;

        public User() {} // default constructor

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }

    private User savedUser = new User("Budi", "budi@example.com", ""); // default user

    // GET /api/user → mengisi form secara otomatis
    @GetMapping("/user")
    public User getUser() {
        return savedUser;
    }

    // POST /api/user → menerima data dari form
    @PostMapping("/user")
    public String saveUser(@RequestBody User user) {
        this.savedUser = user;
        return "User updated successfully!";
    }
}
