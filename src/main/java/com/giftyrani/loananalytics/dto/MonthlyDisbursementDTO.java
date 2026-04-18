package com.giftyrani.loananalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyDisbursementDTO {
    private Integer year;
    private Integer month;
    private Long loanCount;
    private BigDecimal totalDisbursed;
}
