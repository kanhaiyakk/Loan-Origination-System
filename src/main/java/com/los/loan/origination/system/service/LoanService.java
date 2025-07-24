package com.los.loan.origination.system.service;

import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface LoanService {
    Loan submitLoanApplication(Loan loan);
    Page<Loan> getLoansByStatus(ApplicationStatus status, Pageable pageable);
    void processLoan(UUID loanId);
    void processLoans();
    Map<ApplicationStatus, Long> getLoanStatusCounts();
    List<Map<String, Object>> getTopCustomers();
}
