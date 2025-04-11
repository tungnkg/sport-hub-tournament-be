package com.example.billiard_management_be.service.tournament;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import org.apache.coyote.BadRequestException;

public interface ITournamentUseCase {
    void delete(Integer id);
    TournamentResponse getAllByCondition(PageableRequest pageableRequest);
    void create(TournamentRequest request) throws BadRequestException;
    void update(Integer id,TournamentRequest request) throws BadRequestException;
}
