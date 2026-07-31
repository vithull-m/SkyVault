package com.skyvault.service.impl;

import com.skyvault.dto.AircraftRequestDto;
import com.skyvault.dto.AircraftResponseDto;
import com.skyvault.exception.ResourceNotFoundException;
import com.skyvault.exception.SkyVaultApiException;
import com.skyvault.model.Aircraft;
import com.skyvault.repository.AircraftRepository;
import com.skyvault.service.AircraftService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AircraftServiceImpl implements AircraftService {

    private final AircraftRepository aircraftRepository;

    public AircraftServiceImpl(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    @Transactional
    public AircraftResponseDto createAircraft(AircraftRequestDto dto) {
        // Enforce uniqueness constraint check on registration number
        if (aircraftRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new SkyVaultApiException(
                    HttpStatus.CONFLICT,
                    "Aircraft with registration number '" + dto.getRegistrationNumber() + "' already exists."
            );
        }

        Aircraft aircraft = mapToEntity(dto);
        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return mapToResponseDto(savedAircraft);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AircraftResponseDto> getAllAircraft() {
        List<Aircraft> list = aircraftRepository.findAll();
        return list.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AircraftResponseDto getAircraftById(UUID id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "id", id));
        return mapToResponseDto(aircraft);
    }

    @Override
    @Transactional
    public AircraftResponseDto updateAircraft(UUID id, AircraftRequestDto dto) {
        Aircraft existingAircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "id", id));

        // Check registration number conflicts if altered
        if (!existingAircraft.getRegistrationNumber().equalsIgnoreCase(dto.getRegistrationNumber()) &&
                aircraftRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new SkyVaultApiException(
                    HttpStatus.CONFLICT,
                    "Registration number '" + dto.getRegistrationNumber() + "' is already assigned to another aircraft."
            );
        }

        existingAircraft.setRegistrationNumber(dto.getRegistrationNumber());
        existingAircraft.setModel(dto.getModel());
        existingAircraft.setManufacturer(dto.getManufacturer());
        existingAircraft.setAirlineName(dto.getAirlineName());
        existingAircraft.setManufacturingYear(dto.getManufacturingYear());
        existingAircraft.setCapacity(dto.getCapacity());
        existingAircraft.setEngineType(dto.getEngineType());
        existingAircraft.setStatus(dto.getStatus());

        Aircraft updatedAircraft = aircraftRepository.save(existingAircraft);
        return mapToResponseDto(updatedAircraft);
    }

    @Override
    @Transactional
    public void deleteAircraft(UUID id) {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft", "id", id));
        aircraftRepository.delete(aircraft);
    }

    // Entity Mapping Helper Methods
    private Aircraft mapToEntity(AircraftRequestDto dto) {
        Aircraft aircraft = new Aircraft();
        aircraft.setRegistrationNumber(dto.getRegistrationNumber());
        aircraft.setModel(dto.getModel());
        aircraft.setManufacturer(dto.getManufacturer());
        aircraft.setAirlineName(dto.getAirlineName());
        aircraft.setManufacturingYear(dto.getManufacturingYear());
        aircraft.setCapacity(dto.getCapacity());
        aircraft.setEngineType(dto.getEngineType());
        aircraft.setStatus(dto.getStatus());
        return aircraft;
    }

    private AircraftResponseDto mapToResponseDto(Aircraft entity) {
        return new AircraftResponseDto(
                entity.getId(),
                entity.getRegistrationNumber(),
                entity.getModel(),
                entity.getManufacturer(),
                entity.getAirlineName(),
                entity.getManufacturingYear(),
                entity.getCapacity(),
                entity.getEngineType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
