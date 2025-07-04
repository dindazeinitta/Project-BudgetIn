package com.budgetin.web.dto;

import com.budgetin.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public record CreateTransactionDto(
    @NotEmpty(message = "Name cannot be empty")
    String name,
    @NotNull(message = "Type cannot be null")
    TransactionType type,
    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    BigDecimal amount,
    @NotEmpty(message = "Category cannot be empty")
    String category,
    @NotEmpty(message = "Payment method cannot be empty")
    String paymentMethod,
    @NotNull(message = "Transaction date cannot be null")
    Date transactionDateTime,
    String notes
) {}
