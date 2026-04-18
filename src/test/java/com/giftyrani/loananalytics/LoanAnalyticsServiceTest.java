package com.giftyrani.loananalytics;

import com.giftyrani.loananalytics.dto.PortfolioSummaryDTO;
import com.giftyrani.loananalytics.model.Loan;
import com.giftyrani.loananalytics.model.LoanStatus;
import com.giftyrani.loananalytics.repository.LoanRepository;
import com.giftyrani.loananalytics.service.LoanAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanAnalyticsServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanAnalyticsService loanAnalyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPortfolioSummary_returnsCorrectRepaymentRate() {
        when(loanRepository.count()).thenReturn(10L);
        when(loanRepository.countByStatus(LoanStatus.ACTIVE)).thenReturn(7L);
        when(loanRepository.countByStatus(LoanStatus.OVERDUE)).thenReturn(2L);
        when(loanRepository.countPAR30()).thenReturn(2L);
        when(loanRepository.countPAR90()).thenReturn(1L);
        when(loanRepository.getTotalDisbursed()).thenReturn(new BigDecimal("100000"));
        when(loanRepository.getTotalOutstanding()).thenReturn(new BigDecimal("40000"));

        PortfolioSummaryDTO summary = loanAnalyticsService.getPortfolioSummary();

        assertNotNull(summary);
        assertEquals(10L, summary.getTotalLoans());
        assertEquals(7L, summary.getActiveLoans());
        assertEquals(new BigDecimal("60.00"), summary.getRepaymentRatePercent());
        assertEquals(new BigDecimal("20.00"), summary.getPar30Percent());
        assertEquals(new BigDecimal("10.00"), summary.getPar90Percent());
    }

    @Test
    void testGetLoansByStatus_returnsOverdueLoans() {
        Loan overdueLoan = new Loan();
        overdueLoan.setStatus(LoanStatus.OVERDUE);
        overdueLoan.setDaysOverdue(45);

        when(loanRepository.findByStatus(LoanStatus.OVERDUE)).thenReturn(List.of(overdueLoan));

        List<Loan> result = loanAnalyticsService.getLoansByStatus("OVERDUE");

        assertEquals(1, result.size());
        assertEquals(LoanStatus.OVERDUE, result.get(0).getStatus());
    }

    @Test
    void testGetPortfolioSummary_handlesZeroDisbursed() {
        when(loanRepository.count()).thenReturn(0L);
        when(loanRepository.countByStatus(any())).thenReturn(0L);
        when(loanRepository.countPAR30()).thenReturn(0L);
        when(loanRepository.countPAR90()).thenReturn(0L);
        when(loanRepository.getTotalDisbursed()).thenReturn(null);
        when(loanRepository.getTotalOutstanding()).thenReturn(null);

        PortfolioSummaryDTO summary = loanAnalyticsService.getPortfolioSummary();

        assertNotNull(summary);
        assertEquals(BigDecimal.ZERO, summary.getRepaymentRatePercent());
    }
}
