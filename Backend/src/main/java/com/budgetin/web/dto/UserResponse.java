package com.budgetin.web.dto;

public record UserResponse(boolean success, String message, String fullName, String email) {
}