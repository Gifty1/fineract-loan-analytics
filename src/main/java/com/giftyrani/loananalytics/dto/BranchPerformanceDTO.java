package com.giftyrani.loananalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BranchPerformanceDTO {
    private String branch;
    private Long totalLoans;
    private BigDecimal totalDisbursed;
    private BigDecimal totalOutstanding;
    private Long overdueCount;
}
