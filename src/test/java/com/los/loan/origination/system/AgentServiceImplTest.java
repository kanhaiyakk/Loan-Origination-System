package com.los.loan.origination.system;


import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.repository.LoanRepository;
import com.los.loan.origination.system.service.NotificationService;
import com.los.loan.origination.system.serviceImpl.AgentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AgentServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AgentServiceImpl agentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should approve loan and notify customer when decision is APPROVE")
    void testMakeDecisionApprove() {

        Agent agent = new Agent();
        agent.setName("Agent1");


        Loan loan = new Loan();
        loan.setCustomerName("Arjun");
        loan.setLoanAmount(new BigDecimal("50000"));
        loan.setApplicationStatus(ApplicationStatus.UNDER_REVIEW);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        agentService.makeDecision(agent, loan, "APPROVE");

        // Assert
        assertEquals(ApplicationStatus.APPROVED_BY_AGENT, loan.getApplicationStatus());
        verify(loanRepository, times(1)).save(loan);
        verify(notificationService, times(1)).notifyCustomerApproval(loan);
    }

    @Test
    void testMakeDecisionReject() {
        // Arrange
        Agent agent = new Agent();
        agent.setName("Agent2");

        Loan loan = new Loan();
        loan.setCustomerName("Ravi");
        loan.setLoanAmount(new BigDecimal("10000"));
        loan.setApplicationStatus(ApplicationStatus.UNDER_REVIEW);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        agentService.makeDecision(agent, loan, "REJECT");

        // Assert
        assertEquals(ApplicationStatus.REJECTED_BY_AGENT, loan.getApplicationStatus());
        verify(loanRepository, times(1)).save(loan);
        verify(notificationService, never()).notifyCustomerApproval(any(Loan.class));
    }

    @Test
    void testMakeDecisionInvalid() {

        Agent agent = new Agent();
        agent.setName("Agent3");

        Loan loan = new Loan();
        loan.setCustomerName("Amit");
        loan.setLoanAmount(new BigDecimal("15000"));
        loan.setApplicationStatus(ApplicationStatus.UNDER_REVIEW);


        assertThrows(IllegalArgumentException.class, () -> {
            agentService.makeDecision(agent, loan, "HOLD");
        });

        verify(loanRepository, never()).save(any(Loan.class));
        verify(notificationService, never()).notifyCustomerApproval(any(Loan.class));
    }
}
