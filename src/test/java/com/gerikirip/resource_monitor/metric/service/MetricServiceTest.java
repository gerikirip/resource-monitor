package com.gerikirip.resource_monitor.metric.service;

import com.gerikirip.resource_monitor.common.exception.ResourceNotFoundException;
import com.gerikirip.resource_monitor.metric.dto.CreateMetricRequest;
import com.gerikirip.resource_monitor.metric.dto.MetricResponse;
import com.gerikirip.resource_monitor.metric.entity.Metric;
import com.gerikirip.resource_monitor.metric.mapper.MetricMapper;
import com.gerikirip.resource_monitor.metric.repository.MetricRepository;
import com.gerikirip.resource_monitor.server.entity.Server;
import com.gerikirip.resource_monitor.server.repository.ServerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MetricServiceTest {

    @Mock
    private MetricRepository metricRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private MetricMapper metricMapper;

    @InjectMocks
    private MetricService metricService;

    @Test
    void shouldRecordMetricForExistingServer() {
        // Arrange
        Long serverId = 1L;

        CreateMetricRequest request = new CreateMetricRequest(
                75.0,
                60.0,
                40.0
        );

        Server server = new Server(
                "web-server-01",
                "192.168.1.10"
        );

        Metric savedMetric = new Metric(
                server,
                75.0,
                60.0,
                40.0,
                Instant.now()
        );

        MetricResponse expectedResponse = new MetricResponse(
                1L,
                serverId,
                75.0,
                60.0,
                40.0,
                savedMetric.getRecordedAt()
        );

        when(serverRepository.findById(serverId))
                .thenReturn(Optional.of(server));
        when(metricRepository.save(any(Metric.class)))
                .thenReturn(savedMetric);
        when(metricMapper.toResponse(savedMetric))
                .thenReturn(expectedResponse);

        // Act
        MetricResponse response = metricService.recordMetric(serverId, request);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);

        verify(serverRepository).findById(serverId);
        verify(metricRepository).save(any(Metric.class));
        verify(metricMapper).toResponse(savedMetric);
    }

    @Test
    void shouldThrowExceptionWhenServerDoesNotExist() {
        // Arrange
        Long serverId = 1L;

        CreateMetricRequest request = new CreateMetricRequest(
                75.0,
                60.0,
                40.0
        );

        when(serverRepository.findById(serverId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> metricService.recordMetric(serverId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Server with id " + serverId + " not found");

        verify(serverRepository).findById(serverId);
        verifyNoInteractions(metricRepository);
        verifyNoInteractions(metricMapper);
    }
}
