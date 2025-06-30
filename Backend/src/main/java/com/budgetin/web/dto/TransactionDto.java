package com.budgetin.web.dto;

import com.budgetin.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
    Long id,
    String type,
    BigDecimal amount,
    String category,
    String description,
    LocalDateTime transactionDateTime
) {
    public static TransactionDto fromEntity(Transaction transaction) {
        // Explicitly convert Enum to String for clarity
        return new TransactionDto(transaction.getId(), transaction.getType().name(), transaction.getAmount(),
                transaction.getCategory(), transaction.getDescription(), transaction.getTransactionDateTime());
    }
}