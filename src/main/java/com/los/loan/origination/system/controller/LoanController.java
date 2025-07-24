package com.los.loan.origination.system.controller;


import com.los.loan.origination.system.model.ApplicationStatus;
import com.los.loan.origination.system.model.Loan;
import com.los.loan.origination.system.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> submitLoan(@RequestBody Loan loan) {
        Loan savedLoan = loanService.submitLoanApplication(loan);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLoan);
    }

    @PostMapping("/process")
    public ResponseEntity<String> processLoans() {
        loanService.processLoans();
        return ResponseEntity.ok("Loan processing triggered successfully.");
    }

    @GetMapping
    public ResponseEntity<Page<Loan>> getLoansByStatus(
            @RequestParam ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Loan> loans = loanService.getLoansByStatus(status, pageable);
        return ResponseEntity.ok(loans);
    }


    @GetMapping("/status-count")
    public ResponseEntity<Map<ApplicationStatus, Long>> getLoanStatusCounts() {
        Map<ApplicationStatus, Long> counts = loanService.getLoanStatusCounts();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/customers/top")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
        List<Map<String, Object>> customers = loanService.getTopCustomers();
        return ResponseEntity.ok(customers);
    }
}