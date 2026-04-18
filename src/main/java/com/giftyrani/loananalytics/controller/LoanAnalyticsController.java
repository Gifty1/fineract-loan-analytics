package com.giftyrani.loananalytics.controller;

import com.giftyrani.loananalytics.dto.BranchPerformanceDTO;
import com.giftyrani.loananalytics.dto.MonthlyDisbursementDTO;
import com.giftyrani.loananalytics.dto.PortfolioSummaryDTO;
import com.giftyrani.loananalytics.model.Loan;
import com.giftyrani.loananalytics.service.LoanAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Tag(name = "Loan Analytics", description = "Microfinance loan portfolio analytics API")
@CrossOrigin(origins = "*")
public class LoanAnalyticsController {

    private final LoanAnalyticsService loanAnalyticsService;

    @GetMapping("/portfolio/summary")
    @Operation(summary = "Get portfolio summary",
               description = "Returns total loans, repayment rate, PAR30, PAR90 and key metrics")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary() {
        return ResponseEntity.ok(loanAnalyticsService.getPortfolioSummary());
    }

    @GetMapping
    @Operation(summary = "Get all loans")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanAnalyticsService.getAllLoans());
    }

    @GetMapping("/branch/{branch}")
    @Operation(summary = "Get loans by branch")
    public ResponseEntity<List<Loan>> getLoansByBranch(@PathVariable String branch) {
        return ResponseEntity.ok(loanAnalyticsService.getLoansByBranch(branch));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get loans by status",
               description = "Status values: ACTIVE, CLOSED, OVERDUE, WRITTEN_OFF, APPROVED, PENDING")
    public ResponseEntity<List<Loan>> getLoansByStatus(@PathVariable String status) {
        return ResponseEntity.ok(loanAnalyticsService.getLoansByStatus(status));
    }

    @GetMapping("/branch/performance")
    @Operation(summary = "Get branch performance report",
               description = "Aggregated loan counts, disbursements and overdue rates per branch")
    public ResponseEntity<List<BranchPerformanceDTO>> getBranchPerformance() {
        return ResponseEntity.ok(loanAnalyticsService.getBranchPerformance());
    }

    @GetMapping("/disbursements/monthly")
    @Operation(summary = "Get monthly disbursement trend")
    public ResponseEntity<List<MonthlyDisbursementDTO>> getMonthlyDisbursements() {
        return ResponseEntity.ok(loanAnalyticsService.getMonthlyDisbursements());
    }
}
