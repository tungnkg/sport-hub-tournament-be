package com.example.billiard_management_be.service.pair.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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
public class PairPlayTournamentRequest {
    @NotNull
    private Integer player1;
    @NotNull
    private Integer player2;
    @NotNull
    private Integer round;
    @NotNull
    private String tablePlay;
    @NotNull
    private LocalDateTime startedDate;
    @NotNull
    private Integer tournamentId;
}
