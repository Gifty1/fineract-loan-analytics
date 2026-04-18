package com.giftyrani.loananalytics.service;

import com.giftyrani.loananalytics.dto.BranchPerformanceDTO;
import com.giftyrani.loananalytics.dto.MonthlyDisbursementDTO;
import com.giftyrani.loananalytics.dto.PortfolioSummaryDTO;
import com.giftyrani.loananalytics.model.Loan;
import com.giftyrani.loananalytics.model.LoanStatus;
import com.giftyrani.loananalytics.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanAnalyticsService {

    private final LoanRepository loanRepository;

    public PortfolioSummaryDTO getPortfolioSummary() {
        long totalLoans     = loanRepository.count();
        long activeLoans    = loanRepository.countByStatus(LoanStatus.ACTIVE);
        long overdueLoans   = loanRepository.countByStatus(LoanStatus.OVERDUE);
        long par30Count     = loanRepository.countPAR30();
        long par90Count     = loanRepository.countPAR90();

        BigDecimal totalDisbursed   = nullSafe(loanRepository.getTotalDisbursed());
        BigDecimal totalOutstanding = nullSafe(loanRepository.getTotalOutstanding());

        // Repayment rate = (disbursed - outstanding) / disbursed * 100
        BigDecimal repaymentRate = BigDecimal.ZERO;
        BigDecimal par30 = BigDecimal.ZERO;
        BigDecimal par90 = BigDecimal.ZERO;

        if (totalDisbursed.compareTo(BigDecimal.ZERO) > 0) {
            repaymentRate = totalDisbursed.subtract(totalOutstanding)
                    .divide(totalDisbursed, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        if (totalLoans > 0) {
            par30 = BigDecimal.valueOf(par30Count)
                    .divide(BigDecimal.valueOf(totalLoans), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);

            par90 = BigDecimal.valueOf(par90Count)
                    .divide(BigDecimal.valueOf(totalLoans), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return PortfolioSummaryDTO.builder()
                .totalLoans(totalLoans)
                .activeLoans(activeLoans)
                .overdueLoans(overdueLoans)
                .totalDisbursed(totalDisbursed)
                .totalOutstanding(totalOutstanding)
                .repaymentRatePercent(repaymentRate)
                .par30Percent(par30)
                .par90Percent(par90)
                .build();
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public List<Loan> getLoansByBranch(String branch) {
        return loanRepository.findByBranch(branch);
    }

    public List<Loan> getLoansByStatus(String status) {
        return loanRepository.findByStatus(LoanStatus.valueOf(status.toUpperCase()));
    }

    public List<BranchPerformanceDTO> getBranchPerformance() {
        return loanRepository.getBranchPerformance();
    }

    public List<MonthlyDisbursementDTO> getMonthlyDisbursements() {
        return loanRepository.getMonthlyDisbursements();
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
