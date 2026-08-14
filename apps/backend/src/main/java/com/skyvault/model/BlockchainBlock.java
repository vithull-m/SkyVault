package com.skyvault.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "blockchain_ledger_blocks")
@CompoundIndexes({
        @CompoundIndex(name = "idx_ledger_flight_block", def = "{'flight_id': 1, 'block_index': 1}")
})
public class BlockchainBlock {

    @Id
    private Long id;

    @Field("block_index")
    private Long blockIndex;

    @Indexed
    @Field("flight_id")
    private String flightId;

    @Indexed
    @Field("telemetry_id")
    private Long telemetryId;

    @Field("record_hash")
    private String recordHash;

    @Field("previous_hash")
    private String previousHash;

    @Field("current_hash")
    private String currentHash;

    @Field("timestamp")
    private Instant timestamp;

    @Field("verification_status")
    private String verificationStatus = "VALIDATED";
}
