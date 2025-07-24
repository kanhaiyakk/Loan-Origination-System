package com.los.loan.origination.system.service;

import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.Loan;

public interface NotificationService {
    void notifyAgentAssignment(Agent agent, Loan loan);
    void notifyCustomerApproval(Loan loan);
}
