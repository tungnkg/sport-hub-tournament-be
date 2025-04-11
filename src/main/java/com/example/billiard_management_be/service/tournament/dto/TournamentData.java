package com.example.billiard_management_be.service.tournament.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TournamentData {
    private Integer id;
    private String name;
    private String avatar;
    private String introduce;
    private String competitionFormat;
    private Double fee;
    private Integer amountLimit;
    private Double award;
    private String sponsor;
    private Boolean status;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
