package com.los.loan.origination.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class LoanProcessingConfig {

    @Bean
    public ExecutorService loanProcessingExecutor() {
        return Executors.newFixedThreadPool(5);
    }
}
