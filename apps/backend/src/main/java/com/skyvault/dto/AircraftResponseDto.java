package com.skyvault.dto;

import com.skyvault.model.AircraftStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftResponseDto {
    private UUID id;
    private String registrationNumber;
    private String model;
    private String manufacturer;
    private String airlineName;
    private Integer manufacturingYear;
    private Integer capacity;
    private String engineType;
    private AircraftStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
