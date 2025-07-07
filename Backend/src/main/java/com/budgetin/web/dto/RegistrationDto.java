package com.budgetin.web.dto;

import jakarta.validation.constraints.*;

public record RegistrationDto(
    @NotBlank(message = "Username is required")
    String username,
    
    @NotBlank(message = "Full name is required")
    String fullName,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password,
    
    @NotBlank(message = "OTP is required")
    String otp
) {}
