package com.budgetin.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.budgetin.service.UserService;
import com.budgetin.web.dto.RegistrationDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDto registrationDto) {
        try {
            userService.register(registrationDto);
            return ResponseEntity.ok().body(
                new ApiResponse(true, "Registration successful")
            );
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, e.getMessage())
            );
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                new ApiResponse(false, "Error saving user data")
            );
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            boolean isValid = userService.validateUser(loginRequest.email(), loginRequest.password());
            if (isValid) {
                return ResponseEntity.ok().body(
                    new ApiResponse(true, "Login successful")
                );
            } else {
                return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Invalid email or password")
                );
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                new ApiResponse(false, "Error processing login")
            );
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> getUserByEmail(@RequestBody UserRequest userRequest) {
        try {
            Optional<com.budgetin.model.User> userOptional = userService.findByEmail(userRequest.email());
            if (userOptional.isPresent()) {
                com.budgetin.model.User user = userOptional.get();
                return ResponseEntity.ok().body(
                    new UserResponse(true, "User found", user.getFullName(), user.getEmail())
                );
            } else {
                return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "User not found")
                );
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(
                new ApiResponse(false, "Error fetching user data")
            );
        }
    }

    // Helper class untuk response standar
    record ApiResponse(boolean success, String message) {}

    // Helper class untuk login request
    record LoginRequest(String email, String password) {}

    // Helper class untuk user request
    record UserRequest(String email) {}

    // Helper class untuk user response
    record UserResponse(boolean success, String message, String fullName, String email) {}
}
