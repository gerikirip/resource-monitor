package com.gerikirip.resource_monitor.metric.dto;

import lombok.Builder;

@Builder
public record CreateMetricRequest(
        Double cpuUsage,
        Double ramUsage,
        Double diskUsage
) {
}
