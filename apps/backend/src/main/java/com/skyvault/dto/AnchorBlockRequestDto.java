package com.skyvault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnchorBlockRequestDto {

    @NotBlank(message = "Flight ID is required")
    private String flightId;

    @NotNull(message = "Telemetry ID is required")
    private Long telemetryId;
}
