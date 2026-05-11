package com.gerikirip.resource_monitor.server.controller;

import com.gerikirip.resource_monitor.common.exception.DuplicateResourceException;
import com.gerikirip.resource_monitor.common.exception.ResourceNotFoundException;
import com.gerikirip.resource_monitor.server.dto.CreateServerRequest;
import com.gerikirip.resource_monitor.server.dto.ServerResponse;
import com.gerikirip.resource_monitor.server.entity.ServerStatus;
import com.gerikirip.resource_monitor.server.service.ServerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ServerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServerService serverService;

    @Test
    void shouldReturnAllServers() throws Exception {
        // Arrange
        List<ServerResponse> responses = List.of(
                new ServerResponse(
                        1L,
                        "web-server-01",
                        "192.168.1.10",
                        ServerStatus.ONLINE,
                        Instant.now()
                ),
                new ServerResponse(
                        2L,
                        "db-server-01",
                        "192.168.1.20",
                        ServerStatus.WARNING,
                        Instant.now()
                )
        );

        when(serverService.getAllServers())
                .thenReturn(responses);

        // Act + Assert
        mockMvc.perform(get("/api/servers"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].name").value("web-server-01"),
                        jsonPath("$[1].name").value("db-server-01")
                );

        verify(serverService).getAllServers();
    }

    @Test
    void shouldReturnEmptyListWhenNoServersExist() throws Exception {
        // Arrange
        when(serverService.getAllServers()).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/servers"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(0)
                );
    }

    @Test
    void shouldReturnServerById() throws Exception {
        // Arrange
        Long serverId = 1L;

        ServerResponse response = new ServerResponse(
                1L,
                "web-server-01",
                "192.168.1.10",
                ServerStatus.ONLINE,
                Instant.now()
        );

        when(serverService.getServerById(serverId))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/api/servers/{id}", serverId))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("web-server-01"),
                        jsonPath("$.ipAddress").value("192.168.1.10"),
                        jsonPath("$.status").value("ONLINE")
                );
    }

    @Test
    void shouldReturnNotFoundWhenServerDoesNotExist() throws Exception {
        // Arrange
        Long serverId = 1L;

        when(serverService.getServerById(serverId))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Server with id " + serverId + " not found"
                        )
                );

        // Act + Assert
        mockMvc.perform(get("/api/servers/{id}", serverId))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.title").value("Resource not found"),
                        jsonPath("$.detail").value("Server with id 1 not found")
                );

        verify(serverService).getServerById(serverId);
    }


    @Test
    void shouldCreateServer() throws Exception {
        // Arrange
        CreateServerRequest request = new CreateServerRequest(
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

        when(serverService.createServer(request))
                .thenReturn(expectedResponse);

        // Act + Assert
        mockMvc.perform(post("/api/servers/create", request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "name": "web-server-01",
                                        "ipAddress": "192.168.1.10"
                                    }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.name").value("web-server-01"),
                        jsonPath("$.ipAddress").value("192.168.1.10"),
                        jsonPath("$.status").value("ONLINE")
                );

        verify(serverService).createServer(request);
    }

    @Test
    void shouldReturnBadRequestWhenCreateServerRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/servers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name": "",
                                "ipAddress": ""
                            }
                        """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(serverService);
    }

    @Test
    void shouldReturnConflictWhenServerNameAlreadyExists() throws Exception {
        CreateServerRequest request = new CreateServerRequest(
                "web-server-01",
                "192.168.1.10"
        );

        when(serverService.createServer(request))
                .thenThrow(new DuplicateResourceException("Server name already exists"));

        mockMvc.perform(post("/api/servers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "name": "web-server-01",
                                "ipAddress": "192.168.1.10"
                            }
                        """))
                .andExpectAll(
                        status().isConflict(),
                        jsonPath("$.title").value("Duplicate resource"),
                        jsonPath("$.detail").value("Server name already exists")
                );

        verify(serverService).createServer(request);
    }
}
