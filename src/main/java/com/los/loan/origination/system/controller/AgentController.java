package com.los.loan.origination.system.controller;

import com.los.loan.origination.system.dto.DecisionRequest;
import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.repository.AgentRepository;
import com.los.loan.origination.system.repository.LoanRepository;
import com.los.loan.origination.system.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;
    private final AgentRepository agentRepository;
    private final LoanRepository loanRepository;

    @PutMapping("/{agentId}/loans/{loanId}/decision")
    public ResponseEntity<String> agentDecision(
            @PathVariable UUID agentId,
            @PathVariable UUID loanId,
            @RequestBody DecisionRequest request
    ) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        agentService.makeDecision(agent, loan, request.getDecision());
        String message = "Decision [" + request.getDecision() + "] recorded successfully.";

        return ResponseEntity.ok(message);
    }
}
