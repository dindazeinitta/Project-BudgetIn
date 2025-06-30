package com.budgetin.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDto(
    @NotBlank String fullName
) {}