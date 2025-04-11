package com.example.billiard_management_be.service.tournament_register.dto;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindTournamentRegisterRequest extends PageableRequest {
    private Integer tournamentId;
}
