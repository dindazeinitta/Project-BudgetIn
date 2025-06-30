package com.budgetin.web.dto;

import java.util.Map;

public record ErrorResponse(
    boolean success,
    String message,
    Map<String, String> errors
) {
    // Constructor for general errors without field specifics
    public ErrorResponse(boolean success, String message) {
        this(success, message, null);
    }
}