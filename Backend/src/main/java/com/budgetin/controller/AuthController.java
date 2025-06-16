package com.budgetin.controller;

import java.io.IOException;

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

    @PostMapping("/register")
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

    @PostMapping("/login")
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

    // Helper class untuk response standar
    record ApiResponse(boolean success, String message) {}

    // Helper class untuk login request
    record LoginRequest(String email, String password) {}
}
