package com.gerikirip.resource_monitor.server.dto;

import com.gerikirip.resource_monitor.server.entity.ServerStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ServerResponse(
        Long id,
        String name,
        String ipAddress,
        ServerStatus status,
        Instant createdAt
) {
}
