package com.giftyrani.loananalytics.repository;

import com.giftyrani.loananalytics.dto.BranchPerformanceDTO;
import com.giftyrani.loananalytics.dto.MonthlyDisbursementDTO;
import com.giftyrani.loananalytics.model.Loan;
import com.giftyrani.loananalytics.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByBranch(String branch);

    Long countByStatus(LoanStatus status);

    @Query("SELECT SUM(l.disbursedAmount) FROM Loan l")
    BigDecimal getTotalDisbursed();

    @Query("SELECT SUM(l.outstandingBalance) FROM Loan l WHERE l.status = 'ACTIVE'")
    BigDecimal getTotalOutstanding();

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.daysOverdue > 30")
    Long countPAR30();

    @Query("SELECT COUNT(l) FROM Loan l WHERE l.daysOverdue > 90")
    Long countPAR90();

    @Query("""
        SELECT new com.giftyrani.loananalytics.dto.BranchPerformanceDTO(
            l.branch,
            COUNT(l),
            SUM(l.disbursedAmount),
            SUM(l.outstandingBalance),
            SUM(CASE WHEN l.status = 'OVERDUE' THEN 1 ELSE 0 END)
        )
        FROM Loan l GROUP BY l.branch ORDER BY COUNT(l) DESC
    """)
    List<BranchPerformanceDTO> getBranchPerformance();

    @Query("""
        SELECT new com.giftyrani.loananalytics.dto.MonthlyDisbursementDTO(
            YEAR(l.disbursementDate),
            MONTH(l.disbursementDate),
            COUNT(l),
            SUM(l.disbursedAmount)
        )
        FROM Loan l
        WHERE l.disbursementDate IS NOT NULL
        GROUP BY YEAR(l.disbursementDate), MONTH(l.disbursementDate)
        ORDER BY YEAR(l.disbursementDate), MONTH(l.disbursementDate)
    """)
    List<MonthlyDisbursementDTO> getMonthlyDisbursements();
}
