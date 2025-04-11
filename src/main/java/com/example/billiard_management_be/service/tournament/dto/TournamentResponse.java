package com.example.billiard_management_be.service.tournament.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TournamentResponse {
    @Builder.Default private long count = 0;
    @Builder.Default private long total = 0;
    @Builder.Default private List<TournamentData> data = new ArrayList<>();
}