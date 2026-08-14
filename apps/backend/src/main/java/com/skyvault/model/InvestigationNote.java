package com.skyvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investigation_notes", indexes = {
        @Index(name = "idx_note_flight_id", columnList = "flight_id"),
        @Index(name = "idx_note_incident_type", columnList = "incident_type")
})
public class InvestigationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "note_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "flight_id", nullable = false, length = 50)
    private String flightId;

    @Column(name = "investigator_id", nullable = false)
    private UUID investigatorId;

    @Column(name = "investigator_name", nullable = false, length = 100)
    private String investigatorName;

    @Column(name = "incident_type", length = 50)
    private String incidentType;

    @Column(name = "note_text", nullable = false, columnDefinition = "TEXT")
    private String noteText;

    @Column(name = "evidence_summary", columnDefinition = "TEXT")
    private String evidenceSummary;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
