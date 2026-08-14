package com.skyvault.repository;

import com.skyvault.model.InvestigationNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvestigationNoteRepository extends MongoRepository<InvestigationNote, UUID> {

    List<InvestigationNote> findByFlightIdOrderByCreatedAtDesc(String flightId);

    Page<InvestigationNote> findByIncidentType(String incidentType, Pageable pageable);
}
