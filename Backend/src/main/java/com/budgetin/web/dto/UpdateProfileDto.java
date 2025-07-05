package com.budgetin.web.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Email;

public record UpdateProfileDto(
    @NotBlank String fullName,
    @NotBlank @Email String email
) {}
