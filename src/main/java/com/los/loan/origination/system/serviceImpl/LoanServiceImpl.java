package com.los.loan.origination.system.serviceImpl;

import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.repository.LoanRepository;
import com.los.loan.origination.system.service.AgentService;
import com.los.loan.origination.system.service.LoanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final ExecutorService loanProcessingExecutor;
    private final AgentService agentService;

    private final Random random = new Random();

    @Override
    @Transactional
    public Loan submitLoanApplication(Loan loan) {
        loan.setApplicationStatus(ApplicationStatus.APPLIED);
        loan.setCreatedAt(LocalDateTime.now());
        Loan savedLoan = loanRepository.save(loan);

        loanProcessingExecutor.submit(() -> processLoan(savedLoan.getLoanId()));

        return savedLoan;
    }

    public void processLoan(UUID loanId) {
        try {
            Thread.sleep(25000 + random.nextInt(5000));

            Loan loan = loanRepository.findById(loanId)
                    .orElseThrow(() -> new RuntimeException("Loan not found for processing."));

            ApplicationStatus decision = decideLoan();
            if (decision == ApplicationStatus.UNDER_REVIEW) {
                loan.setApplicationStatus(ApplicationStatus.UNDER_REVIEW);
                loanRepository.save(loan);
                agentService.assignAgentToLoan(loan);
            } else {
                loan.setApplicationStatus(decision);
                loanRepository.save(loan);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void processLoans() {
        List<Loan> appliedLoans = loanRepository.findByApplicationStatus(ApplicationStatus.APPLIED);
        for (Loan loan : appliedLoans) {
            processLoan(loan.getLoanId());
        }
    }

    private ApplicationStatus decideLoan() {
        int decision = random.nextInt(3);
        return switch (decision) {
            case 0 -> ApplicationStatus.APPROVED_BY_SYSTEM;
            case 1 -> ApplicationStatus.REJECTED_BY_SYSTEM;
            default -> ApplicationStatus.UNDER_REVIEW;
        };
    }

    @Override
    public Page<Loan> getLoansByStatus(ApplicationStatus status, Pageable pageable) {
        return loanRepository.findByApplicationStatus(status, pageable);
    }

    @Override
    public Map<ApplicationStatus, Long> getLoanStatusCounts() {
        List<Object[]> results = loanRepository.countLoansByStatus();
        Map<ApplicationStatus, Long> statusCounts = new HashMap<>();
        for (Object[] row : results) {
            statusCounts.put((ApplicationStatus) row[0], (Long) row[1]);
        }
        return statusCounts;
    }

    @Override
    public List<Map<String, Object>> getTopCustomers() {
        List<ApplicationStatus> approvedStatuses = List.of(
                ApplicationStatus.APPROVED_BY_SYSTEM,
                ApplicationStatus.APPROVED_BY_AGENT
        );

        List<Object[]> results = loanRepository.findTopCustomers(approvedStatuses);

        List<Map<String, Object>> topCustomers = new ArrayList<>();
        int count = 0;
        for (Object[] row : results) {
            if (count >= 3) break;
            Map<String, Object> data = new HashMap<>();
            data.put("customerName", row[0]);
            data.put("approvedLoans", row[1]);
            topCustomers.add(data);
            count++;
        }
        return topCustomers;
    }


}
