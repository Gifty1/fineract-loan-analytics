package com.loananalytics.dto;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;

@Data
@Builder
public class PortfolioSummaryDTO {

    private long totalLoans;
    private long activeLoans;
    private long overdueLoans;
    private long closedLoans;

    private BigDecimal totalDisbursed;
    private BigDecimal totalOutstanding;
    private BigDecimal totalAtRisk;

    // PAR = Portfolio At Risk
    // PAR30 = loans overdue more than 30 days / total outstanding
    private BigDecimal par30Percentage;
    private BigDecimal par90Percentage;

    private BigDecimal repaymentRatePercentage;

    private String currency;
}
