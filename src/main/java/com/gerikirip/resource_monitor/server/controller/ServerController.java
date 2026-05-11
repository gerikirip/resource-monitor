package com.gerikirip.resource_monitor.server.controller;

import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.service.ServerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public List<ServerResponse> getAllServers() {
        return serverService.getAllServers();
    }

    @GetMapping("/{id}")
    public ServerResponse getServerById(@PathVariable Long id) {
        return serverService.getServerById(id);
    }
}
