package com.loananalytics.controller;

import com.loananalytics.dto.BranchPerformanceDTO;
import com.loananalytics.dto.PortfolioSummaryDTO;
import com.loananalytics.model.Loan;
import com.loananalytics.service.LoanAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Loan Analytics", description = "Microfinance portfolio analytics endpoints")
public class LoanAnalyticsController {

    private final LoanAnalyticsService analyticsService;

    /**
     * GET /api/v1/analytics/portfolio/summary
     * Returns overall portfolio health: PAR30, PAR90, repayment rate, totals.
     */
    @GetMapping("/portfolio/summary")
    @Operation(
        summary = "Get portfolio summary",
        description = "Returns key microfinance KPIs: total loans, PAR30, PAR90, repayment rate, and outstanding balance"
    )
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary() {
        return ResponseEntity.ok(analyticsService.getPortfolioSummary());
    }

    /**
     * GET /api/v1/analytics/branches
     * Returns performance breakdown per branch.
     */
    @GetMapping("/branches")
    @Operation(
        summary = "Get branch performance",
        description = "Returns loan portfolio metrics grouped by branch — useful for regional manager reporting"
    )
    public ResponseEntity<List<BranchPerformanceDTO>> getBranchPerformance() {
        return ResponseEntity.ok(analyticsService.getBranchPerformance());
    }

    /**
     * GET /api/v1/analytics/par30
     * Returns list of all loans overdue by more than 30 days.
     */
    @GetMapping("/par30")
    @Operation(
        summary = "Get PAR30 loans",
        description = "Returns all loans with days in arrears greater than 30 — the at-risk portfolio"
    )
    public ResponseEntity<List<Loan>> getPar30Loans() {
        return ResponseEntity.ok(analyticsService.getPar30Loans());
    }

    /**
     * GET /api/v1/analytics/disbursements?startDate=2024-01-01&endDate=2024-12-31
     * Returns loans disbursed within a date range — used for trend charts.
     */
    @GetMapping("/disbursements")
    @Operation(
        summary = "Get disbursement trend",
        description = "Returns all loans disbursed within the specified date range — used for monthly trend charts in Power BI"
    )
    public ResponseEntity<List<Loan>> getDisbursementTrend(
            @Parameter(description = "Start date in YYYY-MM-DD format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date in YYYY-MM-DD format")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(analyticsService.getDisbursementTrend(startDate, endDate));
    }

    /**
     * GET /api/v1/analytics/health
     * Simple health check endpoint.
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns API status")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Fineract Loan Analytics API is running");
    }
}
