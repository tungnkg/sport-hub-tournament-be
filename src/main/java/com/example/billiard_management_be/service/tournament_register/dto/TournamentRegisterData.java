package com.example.billiard_management_be.service.tournament_register.dto;

import com.example.billiard_management_be.service.auth.dto.PlayerInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TournamentRegisterData {
    private Integer id;
    private Integer playerId;
    private Boolean isPaymentFee;
    private Instant paymentFeeDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer tournamentId;
    private Boolean status;
    private Integer award;
    private String paymentMethod;
    private PlayerInfo playerInfo;
}
