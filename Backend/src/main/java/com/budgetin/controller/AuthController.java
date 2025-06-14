package com.budgetin.controller;

import com.budgetin.service.UserService;
import com.budgetin.web.dto.RegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        }
    }
}

// Helper class untuk response standar
record ApiResponse(boolean success, String message) {}
