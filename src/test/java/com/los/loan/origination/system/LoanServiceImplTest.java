package com.los.loan.origination.system;

import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.model.LoanType;
import com.los.loan.origination.system.repository.LoanRepository;
import com.los.loan.origination.system.serviceImpl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class LoanServiceImplTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ExecutorService loanProcessingExecutor;

    @InjectMocks
    private LoanServiceImpl loanService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitLoanApplication() {

        Loan loan = new Loan();
        loan.setCustomerName("Sunny Shah");
        loan.setCustomerPhone("9784125800");
        loan.setLoanAmount(new BigDecimal("10000"));
        loan.setLoanType(LoanType.PERSONAL);
        loan.setApplicationStatus(ApplicationStatus.APPLIED);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);


        Loan savedLoan = loanService.submitLoanApplication(loan);


        assertNotNull(savedLoan);
        assertEquals("Sunny Shah", savedLoan.getCustomerName());
        assertEquals(ApplicationStatus.APPLIED, savedLoan.getApplicationStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testProcessLoan() {

        UUID loanId = UUID.randomUUID();
        Loan loan = new Loan();
        loan.setCustomerName("Kanhaiya");
        loan.setApplicationStatus(ApplicationStatus.APPLIED);
        loan.setLoanAmount(new BigDecimal("50000"));

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));


        loanService.processLoan(loanId);


        verify(loanRepository, times(1)).findById(loanId);
        verify(loanRepository, times(1)).save(loan);

        assertTrue(
                loan.getApplicationStatus() == ApplicationStatus.APPROVED_BY_SYSTEM ||
                        loan.getApplicationStatus() == ApplicationStatus.REJECTED_BY_SYSTEM ||
                        loan.getApplicationStatus() == ApplicationStatus.UNDER_REVIEW
        );
    }

    @Test
    void testGetLoansByStatus_withPagination() {
        ApplicationStatus status = ApplicationStatus.APPROVED_BY_SYSTEM;
        Pageable pageable = PageRequest.of(0, 2);

        Loan loan1 = new Loan();
        loan1.setCustomerName("Test1");
        loan1.setApplicationStatus(status);

        Loan loan2 = new Loan();
        loan2.setCustomerName("Test2");
        loan2.setApplicationStatus(status);

        Page<Loan> loanPage = new PageImpl<>(List.of(loan1, loan2));

        when(loanRepository.findByApplicationStatus(status, pageable)).thenReturn(loanPage);

        Page<Loan> result = loanService.getLoansByStatus(status, pageable);

        assertEquals(2, result.getContent().size());
        verify(loanRepository, times(1)).findByApplicationStatus(status, pageable);
    }

}
