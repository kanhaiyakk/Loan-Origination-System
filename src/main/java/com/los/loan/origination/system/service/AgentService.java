package com.los.loan.origination.system.service;

import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.Loan;

public interface AgentService {
    Agent assignAgentToLoan(Loan loan);
    void makeDecision(Agent agent, Loan loan, String decision);
}
