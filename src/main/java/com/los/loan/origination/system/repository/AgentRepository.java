package com.los.loan.origination.system.repository;

import com.los.loan.origination.system.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
}
