package com.budgetin.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
    @NotBlank String currentPassword,
    @NotBlank @Size(min = 6, message = "New password must be at least 6 characters") String newPassword
) {}