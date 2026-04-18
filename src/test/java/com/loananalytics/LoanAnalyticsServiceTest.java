package com.loananalytics;

import com.loananalytics.dto.PortfolioSummaryDTO;
import com.loananalytics.model.Loan;
import com.loananalytics.repository.LoanRepository;
import com.loananalytics.service.LoanAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanAnalyticsServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanAnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        when(loanRepository.count()).thenReturn(20L);
        when(loanRepository.countByStatus(Loan.LoanStatus.ACTIVE)).thenReturn(12L);
        when(loanRepository.countByStatus(Loan.LoanStatus.OVERDUE)).thenReturn(5L);
        when(loanRepository.countByStatus(Loan.LoanStatus.CLOSED)).thenReturn(3L);
        when(loanRepository.getTotalDisbursed()).thenReturn(new BigDecimal("112000.00"));
        when(loanRepository.getTotalOutstandingBalance()).thenReturn(new BigDecimal("56300.00"));
        when(loanRepository.getOutstandingBalancePar30()).thenReturn(new BigDecimal("22000.00"));
        when(loanRepository.getOutstandingBalancePar90()).thenReturn(new BigDecimal("6500.00"));
    }

    @Test
    void portfolioSummary_shouldReturnCorrectTotals() {
        PortfolioSummaryDTO summary = analyticsService.getPortfolioSummary();

        assertThat(summary.getTotalLoans()).isEqualTo(20L);
        assertThat(summary.getActiveLoans()).isEqualTo(12L);
        assertThat(summary.getOverdueLoans()).isEqualTo(5L);
        assertThat(summary.getTotalDisbursed()).isEqualByComparingTo("112000.00");
        assertThat(summary.getTotalOutstanding()).isEqualByComparingTo("56300.00");
    }

    @Test
    void portfolioSummary_shouldCalculatePar30Correctly() {
        PortfolioSummaryDTO summary = analyticsService.getPortfolioSummary();

        // PAR30 = 22000 / 56300 * 100 = 39.08%
        assertThat(summary.getPar30Percentage()).isEqualByComparingTo("39.08");
    }

    @Test
    void portfolioSummary_shouldCalculateRepaymentRateCorrectly() {
        PortfolioSummaryDTO summary = analyticsService.getPortfolioSummary();

        // Repayment rate = (112000 - 56300) / 112000 * 100 = 50.27%
        assertThat(summary.getRepaymentRatePercentage()).isEqualByComparingTo("49.73");
    }

    @Test
    void portfolioSummary_shouldHandleNullBalancesGracefully() {
        when(loanRepository.getTotalOutstandingBalance()).thenReturn(null);
        when(loanRepository.getOutstandingBalancePar30()).thenReturn(null);
        when(loanRepository.getOutstandingBalancePar90()).thenReturn(null);

        PortfolioSummaryDTO summary = analyticsService.getPortfolioSummary();

        // Should not throw — nulls become zero
        assertThat(summary.getPar30Percentage()).isEqualByComparingTo("0.00");
        assertThat(summary.getPar90Percentage()).isEqualByComparingTo("0.00");
    }
}
