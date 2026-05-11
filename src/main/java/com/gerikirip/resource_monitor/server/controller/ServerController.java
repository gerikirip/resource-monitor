package com.gerikirip.resource_monitor.server.controller;

import com.gerikirip.resource_monitor.server.dto.CreateServerRequest;
import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.service.ServerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @GetMapping
    public ResponseEntity<List<ServerResponse>> getAllServers() {
        List<ServerResponse> responses = serverService.getAllServers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServerResponse> getServerById(@PathVariable Long id) {
        ServerResponse response = serverService.getServerById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<ServerResponse> createServer(@Valid @RequestBody CreateServerRequest request) {
        ServerResponse response = serverService.createServer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }
}
