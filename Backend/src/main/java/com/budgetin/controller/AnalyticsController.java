package com.budgetin.controller;

import com.budgetin.service.AnalyticsService;
import com.budgetin.web.dto.AnalyticsSummaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryDto> getAnalyticsSummary(Principal principal) {
        AnalyticsSummaryDto summary = analyticsService.getAnalyticsSummary(principal.getName());
        return ResponseEntity.ok(summary);
    }
}
