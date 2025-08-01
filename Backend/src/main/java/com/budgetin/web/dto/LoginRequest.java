package com.budgetin.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@Email String email, @NotEmpty String password, String captchaToken) {
}
