package com.loananalytics.repository;

import com.loananalytics.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    // Find all loans by branch
    List<Loan> findByBranchName(String branchName);

    // Find all overdue loans
    List<Loan> findByStatus(Loan.LoanStatus status);

    // PAR30 — loans with more than 30 days in arrears
    List<Loan> findByDaysInArrearsGreaterThan(int days);

    // Total outstanding balance across all active loans
    @Query("SELECT SUM(l.outstandingBalance) FROM Loan l WHERE l.status = 'ACTIVE'")
    BigDecimal getTotalOutstandingBalance();

    // Total disbursed amount
    @Query("SELECT SUM(l.loanAmount) FROM Loan l")
    BigDecimal getTotalDisbursed();

    // Outstanding balance at risk (PAR30)
    @Query("SELECT SUM(l.outstandingBalance) FROM Loan l WHERE l.daysInArrears > 30")
    BigDecimal getOutstandingBalancePar30();

    // Outstanding balance at risk (PAR90)
    @Query("SELECT SUM(l.outstandingBalance) FROM Loan l WHERE l.daysInArrears > 90")
    BigDecimal getOutstandingBalancePar90();

    // Count by status
    long countByStatus(Loan.LoanStatus status);

    // Branch-level summary — used for branch performance report
    @Query("""
        SELECT new com.loananalytics.dto.BranchPerformanceDTO(
            l.branchName,
            COUNT(l),
            SUM(CASE WHEN l.status = 'OVERDUE' THEN 1 ELSE 0 END),
            SUM(l.loanAmount),
            SUM(l.outstandingBalance),
            0.0,
            0.0
        )
        FROM Loan l
        GROUP BY l.branchName
        """)
    List<BranchPerformanceDTO> getBranchSummary();

    // Disbursements in a date range (for trend charts)
    @Query("SELECT l FROM Loan l WHERE l.disbursedDate BETWEEN :startDate AND :endDate")
    List<Loan> findDisbursedBetween(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}
