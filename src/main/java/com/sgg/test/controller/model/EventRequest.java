package com.sgg.test.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
public class EventRequest {
    @NotNull
    private UUID id;
    @NotNull
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime eventDate;
    @NotNull
    private EventType eventType;
    @NotEmpty
    @Pattern(regexp = "[A-Z]{3}-\\w{3}")
    private String licensePlate;
    private Integer speed;
    private Integer limit;
    private String unity;
    @NotNull
    private Boolean processed;
}