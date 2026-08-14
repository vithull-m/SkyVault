package com.skyvault.service;

import com.skyvault.dto.AnchorBlockRequestDto;
import com.skyvault.dto.BlockResponseDto;
import com.skyvault.dto.FlightChainVerificationResponseDto;
import com.skyvault.dto.RecordVerificationResponseDto;
import com.skyvault.model.BlockchainBlock;
import com.skyvault.model.FlightTelemetry;
import com.skyvault.repository.BlockchainLedgerRepository;
import com.skyvault.repository.TelemetryRepository;
import com.skyvault.service.impl.BlockchainIntegrityServiceImpl;
import com.skyvault.util.CryptoHashUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlockchainIntegrityServiceTest {

    @Mock
    private BlockchainLedgerRepository ledgerRepository;

    @Mock
    private TelemetryRepository telemetryRepository;

    @InjectMocks
    private BlockchainIntegrityServiceImpl blockchainService;

    private FlightTelemetry sampleTelemetry;
    private BlockchainBlock genesisBlock;

    @BeforeEach
    void setUp() {
        UUID aircraftId = UUID.randomUUID();
        Instant now = Instant.parse("2026-08-14T12:00:00Z");

        sampleTelemetry = new FlightTelemetry();
        sampleTelemetry.setId(100L);
        sampleTelemetry.setFlightId("FL-2026-0042");
        sampleTelemetry.setAircraftId(aircraftId);
        sampleTelemetry.setTimestamp(now);
        sampleTelemetry.setFlightPhase("CRUISE");
        sampleTelemetry.setLatitude(40.6413);
        sampleTelemetry.setLongitude(-73.7781);
        sampleTelemetry.setAltitudeFt(33000.0);
        sampleTelemetry.setAirspeedKts(450.0);
        sampleTelemetry.setHeadingDeg(130.0);
        sampleTelemetry.setVerticalSpeedFpm(0.0);
        sampleTelemetry.setFuelLevelLbs(18000.0);
        sampleTelemetry.setEngineRpm(7400.0);
        sampleTelemetry.setEngineTempC(680.0);
        sampleTelemetry.setOatC(-51.0);
        sampleTelemetry.setCabinPressurePsi(11.2);
        sampleTelemetry.setBatteryVolts(28.2);
        sampleTelemetry.setLandingGearStatus("RETRACTED");
        sampleTelemetry.setFlapsDegrees(0.0);
        sampleTelemetry.setAutopilotEngaged(true);

        String recordHash = CryptoHashUtils.applySha256(
                String.format("%s|%s|%s|%.4f|%.4f|%.2f|%.2f|%.2f|%.2f",
                        "FL-2026-0042", aircraftId, now.toString(), 40.6413, -73.7781, 33000.0, 450.0, 130.0, 7400.0)
        );

        String headerData = String.format("0|FL-2026-0042|100|%s|%s|%s",
                recordHash, CryptoHashUtils.GENESIS_PREVIOUS_HASH, now.toString());
        String currentHash = CryptoHashUtils.applySha256(headerData);

        genesisBlock = new BlockchainBlock(
                1L, 0L, "FL-2026-0042", 100L, recordHash, CryptoHashUtils.GENESIS_PREVIOUS_HASH, currentHash, now, "VALIDATED"
        );
    }

    @Test
    @DisplayName("UT-CHAIN-01: Verify valid single record integrity check")
    void verifyRecord_Success() {
        when(ledgerRepository.findByTelemetryId(100L)).thenReturn(Optional.of(genesisBlock));
        when(telemetryRepository.findById(100L)).thenReturn(Optional.of(sampleTelemetry));

        RecordVerificationResponseDto response = blockchainService.verifyRecord(100L);

        assertNotNull(response);
        assertTrue(response.isIntact());
        assertEquals("VALIDATED", response.getStatus());
    }

    @Test
    @DisplayName("UT-CHAIN-02: Detect record payload tampering when altitude is modified")
    void verifyRecord_TamperDetected() {
        // Simulate SQL payload tampering (Altitude changed from 33000 -> 99999)
        sampleTelemetry.setAltitudeFt(99999.0);

        when(ledgerRepository.findByTelemetryId(100L)).thenReturn(Optional.of(genesisBlock));
        when(telemetryRepository.findById(100L)).thenReturn(Optional.of(sampleTelemetry));

        RecordVerificationResponseDto response = blockchainService.verifyRecord(100L);

        assertNotNull(response);
        assertFalse(response.isIntact());
        assertEquals("TAMPER_DETECTED", response.getStatus());
    }

    @Test
    @DisplayName("UT-CHAIN-03: Verify entire valid flight cryptographic chain")
    void verifyFlightChain_Success() {
        when(ledgerRepository.findByFlightIdOrderByBlockIndexAsc("FL-2026-0042")).thenReturn(List.of(genesisBlock));
        when(telemetryRepository.findById(100L)).thenReturn(Optional.of(sampleTelemetry));

        FlightChainVerificationResponseDto response = blockchainService.verifyFlightChain("FL-2026-0042");

        assertNotNull(response);
        assertTrue(response.isChainValid());
        assertEquals(1, response.getTotalBlocksAnalyzed());
        assertEquals("VERIFIED", response.getStatus());
    }
}
