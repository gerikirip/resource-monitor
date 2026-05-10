package com.gerikirip.resource_monitor.server.service;

import com.gerikirip.resource_monitor.common.exception.DuplicateResourceException;
import com.gerikirip.resource_monitor.server.dto.CreateServerRequest;
import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.entity.Server;
import com.gerikirip.resource_monitor.server.entity.ServerStatus;
import com.gerikirip.resource_monitor.server.mapper.ServerMapper;
import com.gerikirip.resource_monitor.server.repository.ServerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private ServerMapper serverMapper;

    @InjectMocks
    private ServerService serverService;

    @Test
    void shouldCreateServerWhenNameIsUnique() {
        // Arrange
        CreateServerRequest request = new CreateServerRequest(
                "web-server-01", "192.168.1.10"
        );

        Server server = new Server(
                "web-server-01",
                "192.168.1.10"
        );

        Server savedServer = new Server(
                "web-server-01",
                "192.168.1.10"
        );

        ServerResponse expectedResponse = new ServerResponse(
                1L,
                "web-server-01",
                "192.168.1.10",
                ServerStatus.ONLINE,
                Instant.now()
        );

        when(serverRepository.existsByName("web-server-01"))
                .thenReturn(false);

        when(serverRepository.save(any(Server.class)))
                .thenReturn(savedServer);

        when(serverMapper.toResponse(savedServer))
                .thenReturn(expectedResponse);

        // Act
        ServerResponse response = serverService.createServer(request);

        // Assert
        assertThat(response).isEqualTo(expectedResponse);

        verify(serverRepository).existsByName("web-server-01");
        verify(serverRepository).save(any(Server.class));
        verify(serverMapper).toResponse(savedServer);
    }

    @Test
    void shouldThrowExceptionWhenServerNameAlreadyExists() {
        // Arrange
        CreateServerRequest request = new CreateServerRequest(
                "web-server-01",
                "192.168.1.10"
        );

        when(serverRepository.existsByName("web-server-01"))
                .thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> serverService.createServer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Server name already exists");

        verify(serverRepository).existsByName("web-server-01");
        verify(serverRepository, never()).save(any(Server.class));
        verifyNoInteractions(serverMapper);
    }

    @Test
    void shouldReturnAllServers() {
        // Arrange
        Server server1 = new Server(
                "web-server-01",
                "192.168.1.10"
        );

        Server server2 = new Server(
                "db-server-01",
                "192.168.1.20"
        );

        List<Server> servers = List.of(server1, server2);

        ServerResponse response1 = new ServerResponse(
                1L,
                "web-server-01",
                "192.168.1.10",
                ServerStatus.ONLINE,
                Instant.now()
        );

        ServerResponse response2 = new ServerResponse(
                2L,
                "db-server-01",
                "192.168.1.20",
                ServerStatus.WARNING,
                Instant.now()
        );

        when(serverRepository.findAll())
                .thenReturn(servers);

        when(serverMapper.toResponse(server1))
                .thenReturn(response1);

        when(serverMapper.toResponse(server2))
                .thenReturn(response2);

        // Act
        List<ServerResponse> responses = serverService.getAllServers();

        // Assert
        assertThat(responses)
                .hasSize(2)
                .containsExactly(response1, response2);

        verify(serverRepository).findAll();

        verify(serverMapper).toResponse(server1);
        verify(serverMapper).toResponse(server2);
    }

    @Test
    void shouldReturnEmptyListWhenNoServersExist() {
        // Arrange
        when(serverRepository.findAll())
                .thenReturn(new ArrayList<>());

        // Act
        List<ServerResponse> responses = serverService.getAllServers();

        // Assert
        assertThat(responses).isEmpty();

        verify(serverRepository).findAll();
    }
}
