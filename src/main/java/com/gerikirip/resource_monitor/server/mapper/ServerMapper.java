package com.gerikirip.resource_monitor.server.mapper;

import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.entity.Server;
import org.springframework.stereotype.Component;

@Component
public class ServerMapper {

    public ServerResponse toResponse(Server server) {
        return ServerResponse.builder()
                .id(server.getId())
                .name(server.getName())
                .ipAddress(server.getIpAddress())
                .status(server.getStatus())
                .createdAt(server.getCreatedAt())
                .build();
    }
}
