package com.gerikirip.resource_monitor.metric.service;

import com.gerikirip.resource_monitor.common.exception.ResourceNotFoundException;
import com.gerikirip.resource_monitor.metric.dto.CreateMetricRequest;
import com.gerikirip.resource_monitor.metric.dto.MetricResponse;
import com.gerikirip.resource_monitor.metric.entity.Metric;
import com.gerikirip.resource_monitor.metric.mapper.MetricMapper;
import com.gerikirip.resource_monitor.metric.repository.MetricRepository;
import com.gerikirip.resource_monitor.server.entity.Server;
import com.gerikirip.resource_monitor.server.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MetricService {

    private final MetricRepository metricRepository;

    private final ServerRepository serverRepository;

    private final MetricMapper metricMapper;

    public MetricService(MetricRepository metricRepository, ServerRepository serverRepository, MetricMapper metricMapper) {
        this.metricRepository = metricRepository;
        this.serverRepository = serverRepository;
        this.metricMapper = metricMapper;
    }

    public MetricResponse recordMetric(Long serverId, CreateMetricRequest request) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Server with id " + serverId + " not found"
                ));

        Metric metric = new Metric(
                server,
                request.cpuUsage(),
                request.ramUsage(),
                request.diskUsage(),
                Instant.now()
        );

        Metric savedMetric = metricRepository.save(metric);

        return metricMapper.toResponse(savedMetric);
    }
}
