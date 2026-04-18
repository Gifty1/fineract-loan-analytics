package com.giftyrani.loananalytics.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PortfolioSummaryDTO {
    private Long totalLoans;
    private Long activeLoans;
    private Long overdueLoans;
    private BigDecimal totalDisbursed;
    private BigDecimal totalOutstanding;
    private BigDecimal repaymentRatePercent;
    private BigDecimal par30Percent;   // Portfolio At Risk > 30 days
    private BigDecimal par90Percent;   // Portfolio At Risk > 90 days
}
