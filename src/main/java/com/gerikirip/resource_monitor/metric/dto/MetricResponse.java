package com.gerikirip.resource_monitor.metric.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record MetricResponse(
        Long id,
        Long serverId,
        Double cpuUsage,
        Double ramUsage,
        Double diskUsage,
        Instant recordedAt
) {
}
