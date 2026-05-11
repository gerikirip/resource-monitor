package com.gerikirip.resource_monitor.metric.mapper;

import com.gerikirip.resource_monitor.metric.dto.MetricResponse;
import com.gerikirip.resource_monitor.metric.entity.Metric;
import org.springframework.stereotype.Component;

@Component
public class MetricMapper {

    public MetricResponse toResponse(Metric metric) {
        return MetricResponse.builder()
                .id(metric.getId())
                .serverId(metric.getServer().getId())
                .cpuUsage(metric.getCpuUsage())
                .ramUsage(metric.getRamUsage())
                .diskUsage(metric.getDiskUsage())
                .recordedAt(metric.getRecordedAt())
                .build();
    }
}
