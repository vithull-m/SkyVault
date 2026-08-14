package com.skyvault.repository;

import com.skyvault.model.BlockchainBlock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockchainLedgerRepository extends MongoRepository<BlockchainBlock, Long> {

    List<BlockchainBlock> findByFlightIdOrderByBlockIndexAsc(String flightId);

    Optional<BlockchainBlock> findFirstByFlightIdOrderByBlockIndexDesc(String flightId);

    Optional<BlockchainBlock> findByTelemetryId(Long telemetryId);

    Page<BlockchainBlock> findByFlightId(String flightId, Pageable pageable);
}
