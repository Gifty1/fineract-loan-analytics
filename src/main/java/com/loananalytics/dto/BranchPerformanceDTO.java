package com.loananalytics.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchPerformanceDTO {

    private String branchName;
    private long totalLoans;
    private long overdueLoans;
    private BigDecimal totalDisbursed;
    private BigDecimal totalOutstanding;
    private BigDecimal par30Percentage;
    private BigDecimal repaymentRatePercentage;
}
