package com.budgetin.service;

import com.budgetin.web.dto.AnalyticsSummaryDto;

public interface AnalyticsService {
    AnalyticsSummaryDto getAnalyticsSummary(String email);
}
