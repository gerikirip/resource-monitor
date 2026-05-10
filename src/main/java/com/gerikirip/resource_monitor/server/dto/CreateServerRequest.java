package com.gerikirip.resource_monitor.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateServerRequest(
        @NotBlank(message = "Server name is required")
        String name,

        @NotBlank(message = "IP address is required")
        @Pattern(
                regexp = "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.|$)){4}$",
                message = "Invalid IPv4 address"
        )
        String ipAddress
) {
}
