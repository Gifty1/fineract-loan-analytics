package com.loananalytics.service;

import com.loananalytics.dto.BranchPerformanceDTO;
import com.loananalytics.dto.PortfolioSummaryDTO;
import com.loananalytics.model.Loan;
import com.loananalytics.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanAnalyticsService {

    private final LoanRepository loanRepository;

    /**
     * Returns full portfolio summary including PAR30, PAR90, repayment rate.
     * These are the key metrics used in Fineract-based microfinance reporting.
     */
    public PortfolioSummaryDTO getPortfolioSummary() {
        log.info("Calculating portfolio summary");

        long totalLoans    = loanRepository.count();
        long activeLoans   = loanRepository.countByStatus(Loan.LoanStatus.ACTIVE);
        long overdueLoans  = loanRepository.countByStatus(Loan.LoanStatus.OVERDUE);
        long closedLoans   = loanRepository.countByStatus(Loan.LoanStatus.CLOSED);

        BigDecimal totalDisbursed   = nullSafe(loanRepository.getTotalDisbursed());
        BigDecimal totalOutstanding = nullSafe(loanRepository.getTotalOutstandingBalance());
        BigDecimal par30Balance     = nullSafe(loanRepository.getOutstandingBalancePar30());
        BigDecimal par90Balance     = nullSafe(loanRepository.getOutstandingBalancePar90());

        // PAR % = outstanding at risk / total outstanding * 100
        BigDecimal par30Pct = calculatePercentage(par30Balance, totalOutstanding);
        BigDecimal par90Pct = calculatePercentage(par90Balance, totalOutstanding);

        // Repayment rate = (total disbursed - total outstanding) / total disbursed * 100
        BigDecimal repaid = totalDisbursed.subtract(totalOutstanding);
        BigDecimal repaymentRate = calculatePercentage(repaid, totalDisbursed);

        return PortfolioSummaryDTO.builder()
                .totalLoans(totalLoans)
                .activeLoans(activeLoans)
                .overdueLoans(overdueLoans)
                .closedLoans(closedLoans)
                .totalDisbursed(totalDisbursed)
                .totalOutstanding(totalOutstanding)
                .totalAtRisk(par30Balance)
                .par30Percentage(par30Pct)
                .par90Percentage(par90Pct)
                .repaymentRatePercentage(repaymentRate)
                .currency("USD")
                .build();
    }

    /**
     * Returns performance metrics per branch.
     * Useful for regional managers to compare branch health.
     */
    public List<BranchPerformanceDTO> getBranchPerformance() {
        log.info("Fetching branch performance data");
        List<BranchPerformanceDTO> branches = loanRepository.getBranchSummary();

        // Calculate PAR30 % and repayment rate per branch
        branches.forEach(branch -> {
            List<Loan> branchLoans = loanRepository.findByBranchName(branch.getBranchName());

            BigDecimal branchOutstanding = branchLoans.stream()
                    .map(Loan::getOutstandingBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal branchPar30 = branchLoans.stream()
                    .filter(l -> l.getDaysInArrears() != null && l.getDaysInArrears() > 30)
                    .map(Loan::getOutstandingBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal branchDisbursed = branchLoans.stream()
                    .map(Loan::getLoanAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            branch.setPar30Percentage(calculatePercentage(branchPar30, branchOutstanding));

            BigDecimal repaid = branchDisbursed.subtract(branchOutstanding);
            branch.setRepaymentRatePercentage(calculatePercentage(repaid, branchDisbursed));
        });

        return branches;
    }

    /**
     * Returns overdue loans — PAR30 list.
     */
    public List<Loan> getPar30Loans() {
        return loanRepository.findByDaysInArrearsGreaterThan(30);
    }

    /**
     * Returns loans disbursed within a given date range.
     * Used for disbursement trend charts in Power BI.
     */
    public List<Loan> getDisbursementTrend(LocalDate startDate, LocalDate endDate) {
        return loanRepository.findDisbursedBetween(startDate, endDate);
    }

    // ---- helpers ----

    private BigDecimal calculatePercentage(BigDecimal part, BigDecimal total) {
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return part.divide(total, 4, RoundingMode.HALF_UP)
                   .multiply(BigDecimal.valueOf(100))
                   .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
