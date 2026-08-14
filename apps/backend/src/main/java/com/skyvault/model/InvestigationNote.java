package com.skyvault.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "investigation_notes")
public class InvestigationNote {

    @Id
    private UUID id;

    @Indexed
    @Field("flight_id")
    private String flightId;

    @Field("investigator_id")
    private UUID investigatorId;

    @Field("investigator_name")
    private String investigatorName;

    @Indexed
    @Field("incident_type")
    private String incidentType;

    @Field("note_text")
    private String noteText;

    @Field("evidence_summary")
    private String evidenceSummary;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
}
