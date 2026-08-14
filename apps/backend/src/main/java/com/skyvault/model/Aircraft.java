package com.skyvault.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "aircraft")
public class Aircraft {

    @Id
    private UUID id;

    @Indexed(unique = true)
    @Field("registration_number")
    private String registrationNumber;

    @Field("model")
    private String model;

    @Field("manufacturer")
    private String manufacturer;

    @Field("airline_name")
    private String airlineName;

    @Field("manufacturing_year")
    private Integer manufacturingYear;

    @Field("capacity")
    private Integer capacity;

    @Field("engine_type")
    private String engineType;

    @Field("status")
    private AircraftStatus status = AircraftStatus.ACTIVE;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
}
