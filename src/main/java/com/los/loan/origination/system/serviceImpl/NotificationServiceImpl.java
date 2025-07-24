package com.los.loan.origination.system.serviceImpl;

import com.los.loan.origination.system.model.Agent;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@ Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notifyAgentAssignment(Agent agent, Loan loan) {
        log.info("[Notification] Agent Assignment: Agent [{}] assigned Loan [{}] for review. Customer: {} Amount: {}",
                agent.getName(), loan.getLoanId(), loan.getCustomerName(), loan.getLoanAmount());
    }

    @Override
    public void notifyCustomerApproval(Loan loan) {
        log.info("[Notification] SMS to Customer [{}]: Your loan [{}] has been approved successfully. Amount: {}",
                loan.getCustomerName(), loan.getLoanId(), loan.getLoanAmount());
    }
}
