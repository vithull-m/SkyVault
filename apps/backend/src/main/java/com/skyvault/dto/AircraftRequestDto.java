package com.skyvault.dto;

import com.skyvault.model.AircraftStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftRequestDto {

    @NotBlank(message = "Registration number is required")
    @Size(min = 3, max = 20, message = "Registration number must be between 3 and 20 characters")
    private String registrationNumber;

    @NotBlank(message = "Aircraft model is required")
    private String model;

    @NotBlank(message = "Manufacturer name is required")
    private String manufacturer;

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotNull(message = "Manufacturing year is required")
    @Min(value = 1900, message = "Manufacturing year must be 1900 or later")
    @Max(value = 2100, message = "Manufacturing year is invalid")
    private Integer manufacturingYear;

    @NotNull(message = "Passenger/Cargo capacity is required")
    @Positive(message = "Capacity must be greater than 0")
    private Integer capacity;

    @NotBlank(message = "Engine type is required")
    private String engineType;

    @NotNull(message = "Aircraft status is required (ACTIVE, MAINTENANCE, RETIRED)")
    private AircraftStatus status;
}
