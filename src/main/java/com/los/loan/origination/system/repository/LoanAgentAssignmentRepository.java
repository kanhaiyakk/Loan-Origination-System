package com.los.loan.origination.system.repository;

import com.los.loan.origination.system.model.LoanAgentAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanAgentAssignmentRepository extends JpaRepository<LoanAgentAssignment, Long> {
}
