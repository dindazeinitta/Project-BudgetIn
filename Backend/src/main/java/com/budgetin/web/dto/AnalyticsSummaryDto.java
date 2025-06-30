package com.budgetin.web.dto;

import java.math.BigDecimal;

public record AnalyticsSummaryDto(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance
) {}
