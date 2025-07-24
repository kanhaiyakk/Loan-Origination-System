package com.los.loan.origination.system.repository;

import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {
    List<Loan> findByApplicationStatus(ApplicationStatus status);

    @Query("SELECT l.applicationStatus, COUNT(l) FROM Loan l GROUP BY l.applicationStatus")
    List<Object[]> countLoansByStatus();

    @Query("SELECT l.customerName, COUNT(l) as cnt FROM Loan l " +
            "WHERE l.applicationStatus IN (:approvedStatuses) " +
            "GROUP BY l.customerName ORDER BY cnt DESC")
    List<Object[]> findTopCustomers(@Param("approvedStatuses") List<ApplicationStatus> approvedStatuses);

    Page<Loan> findByApplicationStatus(ApplicationStatus status, Pageable pageable);
}
