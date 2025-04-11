package com.example.billiard_management_be.controller;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentRequest;
import com.example.billiard_management_be.dto.response.BaseResponse;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import com.example.billiard_management_be.service.tournament.ITournamentUseCase;
import com.example.billiard_management_be.shared.factory.ResponseFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tournament")
@RequiredArgsConstructor
public class TournamentController {
    private final ITournamentUseCase iTournamentUseCase;

    @PostMapping("")
    public ResponseEntity<BaseResponse> create(@RequestBody @Valid TournamentRequest request) throws BadRequestException {
        iTournamentUseCase.create(request);
        return ResponseFactory.success();
    }

    @PutMapping("{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable Integer id,
                                               @RequestBody @Valid TournamentRequest request) throws BadRequestException {
        iTournamentUseCase.update(id, request);
        return ResponseFactory.success();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Integer id) {
        iTournamentUseCase.delete(id);
        return ResponseFactory.success();
    }

    @PostMapping("get-all-by-filter")
    public ResponseEntity<BaseResponse<TournamentResponse>> getAllByCondition(@RequestBody PageableRequest request) {
        return ResponseFactory.success(iTournamentUseCase.getAllByCondition(request));
    }
}
