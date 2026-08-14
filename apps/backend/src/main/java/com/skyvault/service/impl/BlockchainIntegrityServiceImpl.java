package com.skyvault.service.impl;

import com.skyvault.dto.*;
import com.skyvault.exception.ResourceNotFoundException;
import com.skyvault.exception.SkyVaultApiException;
import com.skyvault.model.BlockchainBlock;
import com.skyvault.model.FlightTelemetry;
import com.skyvault.repository.BlockchainLedgerRepository;
import com.skyvault.repository.TelemetryRepository;
import com.skyvault.service.BlockchainIntegrityService;
import com.skyvault.util.CryptoHashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BlockchainIntegrityServiceImpl implements BlockchainIntegrityService {

    private final BlockchainLedgerRepository ledgerRepository;
    private final TelemetryRepository telemetryRepository;

    public BlockchainIntegrityServiceImpl(BlockchainLedgerRepository ledgerRepository,
                                           TelemetryRepository telemetryRepository) {
        this.ledgerRepository = ledgerRepository;
        this.telemetryRepository = telemetryRepository;
    }

    @Override
    @Transactional
    public BlockResponseDto anchorRecord(AnchorBlockRequestDto requestDto) {
        log.info("⛓️ [BLOCKCHAIN ANCHOR] Initiating block creation for Telemetry ID: {} (Flight: {})",
                requestDto.getTelemetryId(), requestDto.getFlightId());

        // 1. Check if record is already anchored
        Optional<BlockchainBlock> existingBlock = ledgerRepository.findByTelemetryId(requestDto.getTelemetryId());
        if (existingBlock.isPresent()) {
            throw new SkyVaultApiException(
                    HttpStatus.CONFLICT,
                    "Telemetry record #" + requestDto.getTelemetryId() + " is already anchored at Block #" + existingBlock.get().getBlockIndex()
            );
        }

        // 2. Fetch target telemetry row from PostgreSQL
        FlightTelemetry telemetry = telemetryRepository.findById(requestDto.getTelemetryId())
                .orElseThrow(() -> new ResourceNotFoundException("FlightTelemetry", "id", requestDto.getTelemetryId()));

        // 3. Compute raw SHA-256 telemetry record hash
        String recordHash = computeTelemetryRecordHash(telemetry);

        // 4. Retrieve previous block in the flight's cryptographic chain
        Optional<BlockchainBlock> lastBlockOpt = ledgerRepository.findFirstByFlightIdOrderByBlockIndexDesc(requestDto.getFlightId());

        long nextIndex = lastBlockOpt.map(block -> block.getBlockIndex() + 1).orElse(0L);
        String previousHash = lastBlockOpt.map(BlockchainBlock::getCurrentHash).orElse(CryptoHashUtils.GENESIS_PREVIOUS_HASH);
        Instant now = Instant.now();

        // 5. Calculate current block hash: SHA-256(index + flightId + telemetryId + recordHash + previousHash + timestamp)
        String blockHeaderData = String.format("%d|%s|%d|%s|%s|%s",
                nextIndex, telemetry.getFlightId(), telemetry.getId(), recordHash, previousHash, now.toString());
        String currentHash = CryptoHashUtils.applySha256(blockHeaderData);

        // 6. Persist new block to DB
        BlockchainBlock block = new BlockchainBlock();
        block.setBlockIndex(nextIndex);
        block.setFlightId(telemetry.getFlightId());
        block.setTelemetryId(telemetry.getId());
        block.setRecordHash(recordHash);
        block.setPreviousHash(previousHash);
        block.setCurrentHash(currentHash);
        block.setTimestamp(now);
        block.setVerificationStatus("VALIDATED");

        BlockchainBlock savedBlock = ledgerRepository.save(block);
        log.info("✅ [BLOCK ANCHORED] Block #{} | Hash: {} | PrevHash: {}",
                savedBlock.getBlockIndex(), savedBlock.getCurrentHash(), savedBlock.getPreviousHash());

        return mapToBlockResponse(savedBlock);
    }

    @Override
    @Transactional(readOnly = true)
    public RecordVerificationResponseDto verifyRecord(Long telemetryId) {
        log.info("🔍 [VERIFY RECORD] Request received for Telemetry ID: {}", telemetryId);

        BlockchainBlock block = ledgerRepository.findByTelemetryId(telemetryId)
                .orElseThrow(() -> new ResourceNotFoundException("BlockchainBlock", "telemetryId", telemetryId));

        FlightTelemetry telemetry = telemetryRepository.findById(telemetryId)
                .orElseThrow(() -> new ResourceNotFoundException("FlightTelemetry", "id", telemetryId));

        // Recompute record hash from database row
        String recalculatedRecordHash = computeTelemetryRecordHash(telemetry);
        boolean isRecordIntact = block.getRecordHash().equals(recalculatedRecordHash);

        // Recompute current block header hash
        String headerData = String.format("%d|%s|%d|%s|%s|%s",
                block.getBlockIndex(), block.getFlightId(), block.getTelemetryId(),
                block.getRecordHash(), block.getPreviousHash(), block.getTimestamp().toString());
        String recalculatedCurrentHash = CryptoHashUtils.applySha256(headerData);
        boolean isBlockIntact = block.getCurrentHash().equals(recalculatedCurrentHash);

        boolean totalPass = isRecordIntact && isBlockIntact;

        String msg = totalPass
                ? "Cryptographic verification passed: Telemetry record and block header are authentic."
                : "TAMPER DETECTED! Database record payload or block header has been altered!";

        log.info("{} Telemetry ID #{} -> Status: {}", totalPass ? "✅" : "🚨", telemetryId, totalPass ? "PASS" : "FAIL");

        return RecordVerificationResponseDto.builder()
                .telemetryId(telemetryId)
                .flightId(block.getFlightId())
                .blockIndex(block.getBlockIndex())
                .isIntact(totalPass)
                .storedRecordHash(block.getRecordHash())
                .recalculatedRecordHash(recalculatedRecordHash)
                .storedCurrentHash(block.getCurrentHash())
                .recalculatedCurrentHash(recalculatedCurrentHash)
                .status(totalPass ? "VALIDATED" : "TAMPER_DETECTED")
                .message(msg)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public FlightChainVerificationResponseDto verifyFlightChain(String flightId) {
        log.info("🔗 [VERIFY CHAIN] Auditing complete cryptographic hash chain for Flight ID: {}", flightId);

        List<BlockchainBlock> chain = ledgerRepository.findByFlightIdOrderByBlockIndexAsc(flightId);
        List<String> auditLogs = new ArrayList<>();

        if (chain.isEmpty()) {
            auditLogs.add("No blockchain ledger records found for flight " + flightId);
            return FlightChainVerificationResponseDto.builder()
                    .flightId(flightId)
                    .totalBlocksAnalyzed(0)
                    .isChainValid(true)
                    .status("EMPTY_CHAIN")
                    .message("No anchored blocks found for this flight.")
                    .auditLogs(auditLogs)
                    .build();
        }

        for (int i = 0; i < chain.size(); i++) {
            BlockchainBlock currentBlock = chain.get(i);
            auditLogs.add(String.format("Auditing Block #%d (Telemetry #%d)...", currentBlock.getBlockIndex(), currentBlock.getTelemetryId()));

            // Step 1: Verify PostgreSQL Telemetry Row Data Intactness
            Optional<FlightTelemetry> telemetryOpt = telemetryRepository.findById(currentBlock.getTelemetryId());
            if (telemetryOpt.isEmpty()) {
                auditLogs.add(String.format("🚨 CRITICAL: Telemetry ID #%d missing from database!", currentBlock.getTelemetryId()));
                return buildTamperedChainResponse(flightId, chain.size(), currentBlock, "MISSING_RECORD", auditLogs);
            }

            String recalculatedRecordHash = computeTelemetryRecordHash(telemetryOpt.get());
            if (!currentBlock.getRecordHash().equals(recalculatedRecordHash)) {
                auditLogs.add(String.format("🚨 TAMPER DETECTED at Block #%d! Record Hash Mismatch. Stored: %s | Computed: %s",
                        currentBlock.getBlockIndex(), currentBlock.getRecordHash(), recalculatedRecordHash));
                return buildTamperedChainResponse(flightId, chain.size(), currentBlock, "RECORD_TAMPERED", auditLogs);
            }

            // Step 2: Verify Current Block Header Integrity
            String headerData = String.format("%d|%s|%d|%s|%s|%s",
                    currentBlock.getBlockIndex(), currentBlock.getFlightId(), currentBlock.getTelemetryId(),
                    currentBlock.getRecordHash(), currentBlock.getPreviousHash(), currentBlock.getTimestamp().toString());
            String recalculatedCurrentHash = CryptoHashUtils.applySha256(headerData);
            if (!currentBlock.getCurrentHash().equals(recalculatedCurrentHash)) {
                auditLogs.add(String.format("🚨 TAMPER DETECTED at Block #%d! Current Hash Mismatch. Stored: %s | Computed: %s",
                        currentBlock.getBlockIndex(), currentBlock.getCurrentHash(), recalculatedCurrentHash));
                return buildTamperedChainResponse(flightId, chain.size(), currentBlock, "BLOCK_HEADER_TAMPERED", auditLogs);
            }

            // Step 3: Verify Previous Hash Pointer Linkage
            if (i == 0) {
                if (!currentBlock.getPreviousHash().equals(CryptoHashUtils.GENESIS_PREVIOUS_HASH)) {
                    auditLogs.add(String.format("🚨 TAMPER DETECTED at Genesis Block #0! Previous hash is invalid: %s", currentBlock.getPreviousHash()));
                    return buildTamperedChainResponse(flightId, chain.size(), currentBlock, "INVALID_GENESIS_LINK", auditLogs);
                }
            } else {
                BlockchainBlock previousBlock = chain.get(i - 1);
                if (!currentBlock.getPreviousHash().equals(previousBlock.getCurrentHash())) {
                    auditLogs.add(String.format("🚨 TAMPER DETECTED at Block #%d! Broken chain linkage pointer. Expected prevHash: %s | Found: %s",
                            currentBlock.getBlockIndex(), previousBlock.getCurrentHash(), currentBlock.getPreviousHash()));
                    return buildTamperedChainResponse(flightId, chain.size(), currentBlock, "BROKEN_CHAIN_LINK", auditLogs);
                }
            }
            auditLogs.add(String.format("✅ Block #%d Verified: Intact & Linked.", currentBlock.getBlockIndex()));
        }

        auditLogs.add(String.format("🎉 Full cryptographic chain verification PASSED for Flight %s (%d blocks).", flightId, chain.size()));
        log.info("✅ [CHAIN VERIFIED] Flight {} -> {} blocks 100% Intact", flightId, chain.size());

        return FlightChainVerificationResponseDto.builder()
                .flightId(flightId)
                .totalBlocksAnalyzed(chain.size())
                .isChainValid(true)
                .tamperedBlockIndex(null)
                .tamperedTelemetryId(null)
                .status("VERIFIED")
                .message("All blocks in the flight chain are cryptographically valid and linked.")
                .auditLogs(auditLogs)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<BlockResponseDto> getVerificationHistory(String flightId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("blockIndex").ascending());
        Page<BlockchainBlock> page = ledgerRepository.findByFlightId(flightId, pageable);
        return PageResponseDto.fromPage(page.map(this::mapToBlockResponse));
    }

    private FlightChainVerificationResponseDto buildTamperedChainResponse(
            String flightId, int totalBlocks, BlockchainBlock tamperedBlock, String reason, List<String> auditLogs) {

        log.warn("🚨 [TAMPER ALERT] Chain verification failed for flight {} at Block #{} (Telemetry #{}) - Reason: {}",
                flightId, tamperedBlock.getBlockIndex(), tamperedBlock.getTelemetryId(), reason);

        return FlightChainVerificationResponseDto.builder()
                .flightId(flightId)
                .totalBlocksAnalyzed(totalBlocks)
                .isChainValid(false)
                .tamperedBlockIndex(tamperedBlock.getBlockIndex())
                .tamperedTelemetryId(tamperedBlock.getTelemetryId())
                .status("TAMPER_DETECTED")
                .message(String.format("Chain integrity compromise detected at Block #%d (Telemetry #%d). Reason: %s",
                        tamperedBlock.getBlockIndex(), tamperedBlock.getTelemetryId(), reason))
                .auditLogs(auditLogs)
                .build();
    }

    private String computeTelemetryRecordHash(FlightTelemetry telemetry) {
        String raw = String.format("%s|%s|%s|%.4f|%.4f|%.2f|%.2f|%.2f|%.2f",
                telemetry.getFlightId(),
                telemetry.getAircraftId(),
                telemetry.getTimestamp().toString(),
                telemetry.getLatitude(),
                telemetry.getLongitude(),
                telemetry.getAltitudeFt(),
                telemetry.getAirspeedKts(),
                telemetry.getHeadingDeg(),
                telemetry.getEngineRpm());
        return CryptoHashUtils.applySha256(raw);
    }

    private BlockResponseDto mapToBlockResponse(BlockchainBlock block) {
        return BlockResponseDto.builder()
                .blockId(block.getId())
                .blockIndex(block.getBlockIndex())
                .flightId(block.getFlightId())
                .telemetryId(block.getTelemetryId())
                .recordHash(block.getRecordHash())
                .previousHash(block.getPreviousHash())
                .currentHash(block.getCurrentHash())
                .timestamp(block.getTimestamp())
                .verificationStatus(block.getVerificationStatus())
                .build();
    }
}
