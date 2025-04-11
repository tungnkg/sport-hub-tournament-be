package com.example.billiard_management_be.service.tournament_register;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.tournament_register.dto.FindTournamentRegisterRequest;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterResponse;

public interface ITournamentRegisterUseCase {
    void register(TournamentRegisterRequest request);

    void cancel(Integer id);

    TournamentRegisterResponse getAllByCondition(FindTournamentRegisterRequest request);
}
