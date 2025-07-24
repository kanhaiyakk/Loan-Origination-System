package com.los.loan.origination.system.serviceImpl;

import com.los.loan.origination.system.exception.ResourceNotFoundException;
import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.model.LoanAgentAssignment;
import com.los.loan.origination.system.repository.AgentRepository;
import com.los.loan.origination.system.repository.LoanAgentAssignmentRepository;
import com.los.loan.origination.system.repository.LoanRepository;
import com.los.loan.origination.system.service.AgentService;
import com.los.loan.origination.system.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {


    private final AgentRepository agentRepository;
    private final LoanAgentAssignmentRepository assignmentRepository;
    private final LoanRepository loanRepository;
    private final NotificationService notificationService;

    private final Random random = new Random();

    @Override
    @Transactional
    public Agent assignAgentToLoan(Loan loan) {
        List<Agent> agents = agentRepository.findAll();

        if (agents.isEmpty()) {
            throw new ResourceNotFoundException("No agents available for assignment.");
        }

        Agent assignedAgent = agents.get(random.nextInt(agents.size()));

        LoanAgentAssignment assignment = LoanAgentAssignment.builder()
                .agent(assignedAgent)
                .loan(loan)
                .assignedAt(LocalDateTime.now())
                .build();

        assignmentRepository.save(assignment);

        notificationService.notifyAgentAssignment(assignedAgent, loan);
        if (assignedAgent.getManager() != null) {
            notificationService.notifyAgentAssignment(assignedAgent.getManager(), loan);
        }

        return assignedAgent;
    }

    @Override
    @Transactional
    public void makeDecision(Agent agent, Loan loan, String decision) {
        if ("APPROVE".equalsIgnoreCase(decision)) {
            loan.setApplicationStatus(ApplicationStatus.APPROVED_BY_AGENT);
            notificationService.notifyCustomerApproval(loan);
        } else if ("REJECT".equalsIgnoreCase(decision)) {
            loan.setApplicationStatus(ApplicationStatus.REJECTED_BY_AGENT);
        } else {
            throw new IllegalArgumentException("Invalid decision. Use APPROVE or REJECT.");
        }
        loanRepository.save(loan);
    }
}
