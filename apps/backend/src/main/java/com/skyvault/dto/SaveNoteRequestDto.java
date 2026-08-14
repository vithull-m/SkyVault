package com.skyvault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveNoteRequestDto {

    @NotBlank(message = "Investigation note text is required")
    private String noteText;

    private String evidenceSummary;

    private String incidentType;
}
