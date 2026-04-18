package com.loananalytics.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "loan_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "outstanding_balance", precision = 15, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(name = "disbursed_date")
    private LocalDate disbursedDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "last_payment_date")
    private LocalDate lastPaymentDate;

    @Column(name = "days_in_arrears")
    private Integer daysInArrears;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus status;

    @Column(name = "loan_product")
    private String loanProduct;

    @Column(name = "interest_rate", precision = 5, scale = 2)
    private BigDecimal interestRate;

    public enum LoanStatus {
        ACTIVE, CLOSED, WRITTEN_OFF, OVERDUE
    }
}
