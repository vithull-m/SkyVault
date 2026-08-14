package com.skyvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blockchain_ledger_blocks", indexes = {
        @Index(name = "idx_ledger_flight_id", columnList = "flight_id"),
        @Index(name = "idx_ledger_telemetry_id", columnList = "telemetry_id"),
        @Index(name = "idx_ledger_flight_block", columnList = "flight_id, block_index ASC")
})
public class BlockchainBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;

    @Column(name = "block_index", nullable = false)
    private Long blockIndex;

    @Column(name = "flight_id", nullable = false, length = 50)
    private String flightId;

    @Column(name = "telemetry_id", nullable = false)
    private Long telemetryId;

    @Column(name = "record_hash", nullable = false, length = 64)
    private String recordHash;

    @Column(name = "previous_hash", nullable = false, length = 64)
    private String previousHash;

    @Column(name = "current_hash", nullable = false, length = 64)
    private String currentHash;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "verification_status", nullable = false, length = 30)
    private String verificationStatus = "VALIDATED";
}
