package com.gerikirip.resource_monitor.metric.entity;

import com.gerikirip.resource_monitor.server.entity.Server;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "metrics")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    private Double cpuUsage;

    private Double ramUsage;

    private Double diskUsage;

    private Instant recordedAt;

    public Metric(Server server, Double ramUsage, Double cpuUsage, Double diskUsage, Instant recordedAt) {
        this.server = server;
        this.ramUsage = ramUsage;
        this.cpuUsage = cpuUsage;
        this.diskUsage = diskUsage;
        this.recordedAt = recordedAt;
    }
}
