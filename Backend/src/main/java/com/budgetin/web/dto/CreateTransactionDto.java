package com.budgetin.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateTransactionDto(
    @NotEmpty(message = "Type cannot be empty")
    String type,
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    BigDecimal amount,
    String category,
    String description
) {}