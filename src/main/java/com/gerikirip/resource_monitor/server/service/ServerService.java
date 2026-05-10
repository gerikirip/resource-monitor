package com.gerikirip.resource_monitor.server.service;

import com.gerikirip.resource_monitor.common.exception.DuplicateResourceException;
import com.gerikirip.resource_monitor.common.exception.ResourceNotFoundException;
import com.gerikirip.resource_monitor.server.dto.CreateServerRequest;
import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.entity.Server;
import com.gerikirip.resource_monitor.server.mapper.ServerMapper;
import com.gerikirip.resource_monitor.server.repository.ServerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    private final ServerRepository serverRepository;
    private final ServerMapper serverMapper;

    public ServerService(ServerRepository serverRepository,
                         ServerMapper serverMapper) {
        this.serverRepository = serverRepository;
        this.serverMapper = serverMapper;
    }

    public ServerResponse createServer(CreateServerRequest request) {

        if (serverRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Server name already exists");
        }

        Server server = new Server(
            request.name(),
            request.ipAddress()
        );

        Server savedServer = serverRepository.save(server);

        return serverMapper.toResponse(savedServer);
    }

    public List<ServerResponse> getAllServers() {
        return serverRepository
                .findAll()
                .stream()
                .map(serverMapper::toResponse)
                .toList();
    }

    public void deleteServer(Long id) {
        if (!serverRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Server with id " + id + " not found"
            );
        }

        serverRepository.deleteById(id);
    }
}
