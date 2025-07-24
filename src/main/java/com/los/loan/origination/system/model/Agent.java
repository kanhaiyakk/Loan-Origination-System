package com.los.loan.origination.system.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID agentId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Agent manager;
}
