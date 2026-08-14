package com.skyvault.service.impl;

import com.skyvault.dto.*;
import com.skyvault.exception.ResourceNotFoundException;
import com.skyvault.mapper.TelemetryMapper;
import com.skyvault.model.FlightTelemetry;
import com.skyvault.model.InvestigationNote;
import com.skyvault.model.User;
import com.skyvault.repository.AircraftRepository;
import com.skyvault.repository.InvestigationNoteRepository;
import com.skyvault.repository.TelemetryRepository;
import com.skyvault.repository.UserRepository;
import com.skyvault.service.AircraftService;
import com.skyvault.service.BlockchainIntegrityService;
import com.skyvault.service.InvestigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvestigationServiceImpl implements InvestigationService {

    private final TelemetryRepository telemetryRepository;
    private final AircraftRepository aircraftRepository;
    private final AircraftService aircraftService;
    private final BlockchainIntegrityService blockchainService;
    private final InvestigationNoteRepository noteRepository;
    private final UserRepository userRepository;

    public InvestigationServiceImpl(TelemetryRepository telemetryRepository,
                                     AircraftRepository aircraftRepository,
                                     AircraftService aircraftService,
                                     BlockchainIntegrityService blockchainService,
                                     InvestigationNoteRepository noteRepository,
                                     UserRepository userRepository) {
        this.telemetryRepository = telemetryRepository;
        this.aircraftRepository = aircraftRepository;
        this.aircraftService = aircraftService;
        this.blockchainService = blockchainService;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PageResponseDto<TelemetryResponseDto> searchInvestigations(InvestigationSearchRequestDto searchDto) {
        log.info("🔎 [INVESTIGATION SEARCH] Querying flights - FlightID: {}, AircraftID: {}, IncidentType: {}",
                searchDto.getFlightId(), searchDto.getAircraftId(), searchDto.getIncidentType());

        Pageable pageable = PageRequest.of(searchDto.getPageNo(), searchDto.getPageSize(), Sort.by("timestamp").descending());
        Page<FlightTelemetry> page;

        if (searchDto.getFlightId() != null && !searchDto.getFlightId().isBlank()) {
            page = telemetryRepository.findByFlightId(searchDto.getFlightId(), pageable);
        } else if (searchDto.getAircraftId() != null) {
            page = telemetryRepository.findByAircraftId(searchDto.getAircraftId(), pageable);
        } else {
            page = telemetryRepository.findAll(pageable);
        }

        return PageResponseDto.fromPage(page.map(TelemetryMapper::toResponseDto));
    }

    @Override
    public InvestigationDetailResponseDto getInvestigationDetails(String flightId) {
        log.info("📋 [INVESTIGATION DETAILS] Fetching audit file for Flight ID: {}", flightId);

        // Fetch telemetry frames for the flight
        Pageable pageable = PageRequest.of(0, 100, Sort.by("timestamp").ascending());
        Page<FlightTelemetry> telemetryPage = telemetryRepository.findByFlightId(flightId, pageable);

        if (telemetryPage.isEmpty()) {
            throw new ResourceNotFoundException("FlightTelemetry", "flightId", flightId);
        }

        List<TelemetryResponseDto> telemetryHistory = telemetryPage.getContent().stream()
                .map(TelemetryMapper::toResponseDto)
                .collect(Collectors.toList());

        // Aircraft metadata
        UUID aircraftId = telemetryPage.getContent().get(0).getAircraftId();
        AircraftResponseDto aircraftDetails = aircraftService.getAircraftById(aircraftId);

        // Cryptographic Chain Integrity status
        FlightChainVerificationResponseDto integrityStatus = blockchainService.verifyFlightChain(flightId);

        // Saved Investigation Notes
        List<InvestigationNote> notes = noteRepository.findByFlightIdOrderByCreatedAtDesc(flightId);

        String evidenceSummary = notes.isEmpty() ? "No preliminary notes filed." : notes.get(0).getEvidenceSummary();

        return InvestigationDetailResponseDto.builder()
                .flightId(flightId)
                .aircraftDetails(aircraftDetails)
                .integrityStatus(integrityStatus)
                .telemetryHistory(telemetryHistory)
                .notes(notes)
                .evidenceSummary(evidenceSummary)
                .totalTelemetryFrames(telemetryHistory.size())
                .build();
    }

    @Override
    public InvestigationNote saveInvestigationNote(String flightId, SaveNoteRequestDto requestDto, String investigatorUsername) {
        log.info("📝 [SAVE NOTE] Investigator '{}' adding note to Flight ID: {}", investigatorUsername, flightId);

        User investigator = userRepository.findByUsername(investigatorUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", investigatorUsername));

        InvestigationNote note = new InvestigationNote();
        note.setId(UUID.randomUUID());
        note.setFlightId(flightId);
        note.setInvestigatorId(investigator.getId());
        note.setInvestigatorName(investigator.getFirstName() + " " + investigator.getLastName());
        note.setIncidentType(requestDto.getIncidentType());
        note.setNoteText(requestDto.getNoteText());
        note.setEvidenceSummary(requestDto.getEvidenceSummary());

        InvestigationNote savedNote = noteRepository.save(note);
        log.info("✅ Investigation note saved successfully with ID: {}", savedNote.getId());
        return savedNote;
    }

    @Override
    public InvestigationReportResponseDto generateReport(String flightId) {
        log.info("📄 [GENERATE REPORT] Compiling official safety investigation report for Flight: {}", flightId);

        InvestigationDetailResponseDto details = getInvestigationDetails(flightId);

        List<String> timelineEvents = new ArrayList<>();
        timelineEvents.add("Flight session initialized and telemetry ingestion started.");
        if (!details.getTelemetryHistory().isEmpty()) {
            TelemetryResponseDto first = details.getTelemetryHistory().get(0);
            TelemetryResponseDto last = details.getTelemetryHistory().get(details.getTelemetryHistory().size() - 1);
            timelineEvents.add("Initial frame logged at " + first.getTimestamp() + " (Alt: " + first.getAltitudeFt() + " ft, Speed: " + first.getAirspeedKts() + " kts).");
            timelineEvents.add("Final frame logged at " + last.getTimestamp() + " (Alt: " + last.getAltitudeFt() + " ft, Speed: " + last.getAirspeedKts() + " kts).");
        }

        List<String> aiFindings = new ArrayList<>();
        if (!details.getIntegrityStatus().isChainValid()) {
            aiFindings.add("ALERT: Cryptographic chain verification failed at Block #" + details.getIntegrityStatus().getTamperedBlockIndex() + ".");
        } else {
            aiFindings.add("All telemetry frames successfully verified against Merkle hash-chain ledger.");
        }

        String reportNum = "INV-REP-" + flightId + "-" + System.currentTimeMillis() % 10000;

        return InvestigationReportResponseDto.builder()
                .reportNumber(reportNum)
                .generatedAt(LocalDateTime.now())
                .flightId(flightId)
                .aircraftDetails(details.getAircraftDetails())
                .flightRoute(details.getAircraftDetails().getAirlineName() + " Operations")
                .totalFramesRecorded(details.getTotalTelemetryFrames())
                .timelineEvents(timelineEvents)
                .aiFindings(aiFindings)
                .integrityResult(details.getIntegrityStatus())
                .finalNotes(details.getNotes())
                .primaryInvestigator("Government Investigation Agency - Senior Air Safety Inspector")
                .reportClassification("OFFICIAL / SAFETY CONFIDENTIAL")
                .build();
    }
}
