package com.skyvault.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationSearchRequestDto {
    private String flightId;
    private UUID aircraftId;
    private Instant startDate;
    private Instant endDate;
    private String incidentType;
    private int pageNo = 0;
    private int pageSize = 20;
}
