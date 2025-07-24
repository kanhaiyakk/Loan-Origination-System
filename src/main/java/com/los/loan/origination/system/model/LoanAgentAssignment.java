package com.los.loan.origination.system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_agent_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAgentAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Agent agent;

    @ManyToOne
    private Loan loan;

    private LocalDateTime assignedAt;
}
