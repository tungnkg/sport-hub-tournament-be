package com.example.billiard_management_be.service.tournament.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TournamentRequest {
    @NotNull(message = "name cannot be null")
    @Size(max = 255, message = "avatar không quá 255 ký tự")
    private String avatar;
    @NotNull(message = "name cannot be null")
    @Size(max = 255, message = "name không quá 255 ký tự")
    private String name;
    @NotNull(message = "introduce cannot be null")
    @Size(max = 2000, message = "introduce không quá 500 ký tự")
    private String introduce;
    @NotNull(message = "competition_format cannot be null")
    @Size(max = 2000, message = "competition_format không quá 2000 ký tự")
    private String competitionFormat;
    @NotNull(message = "fee cannot be null")
    private Double fee;
    @NotNull(message = "amount_limit cannot be null")
    private Integer amountLimit;
    @NotNull(message = "award cannot be null")
    private Double award;
    private String sponsor;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private Integer status;
}
