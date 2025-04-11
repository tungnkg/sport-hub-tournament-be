package com.example.billiard_management_be.service.tournament_register.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TournamentRegisterRequest {
    @NotNull(message = "Player ID cannot be null")
    private Integer playerId;
    @NotNull(message = "Payment fee cannot be null")
    private Boolean isPaymentFee;
    private Instant paymentFeeDate;
    @NotNull(message = "Tournament ID cannot be null")
    private Integer tournamentId;
    @NotNull(message = "Payment method cannot be null")
    private String paymentMethod;
}
