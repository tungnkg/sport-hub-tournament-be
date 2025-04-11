package com.example.billiard_management_be.controller;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.tournament_register.dto.FindTournamentRegisterRequest;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterRequest;
import com.example.billiard_management_be.dto.response.BaseResponse;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import com.example.billiard_management_be.service.tournament_register.ITournamentRegisterUseCase;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterResponse;
import com.example.billiard_management_be.shared.factory.ResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tournament-register")
@RequiredArgsConstructor
public class TournamentRegisterController {
    private final ITournamentRegisterUseCase iTournamentRegisterUseCase;

    @PostMapping("")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid TournamentRegisterRequest request) {
        iTournamentRegisterUseCase.register(request);
        return ResponseFactory.success();
    }

    @PutMapping("cancel/{id}")
    public ResponseEntity<BaseResponse> cancel(@PathVariable Integer id) {
        iTournamentRegisterUseCase.cancel(id);
        return ResponseFactory.success();
    }

    @PostMapping("get-all-by-filter")
    public ResponseEntity<BaseResponse<TournamentRegisterResponse>> getAllByCondition(@RequestBody FindTournamentRegisterRequest request) {
        return ResponseFactory.success(iTournamentRegisterUseCase.getAllByCondition(request));
    }
}
