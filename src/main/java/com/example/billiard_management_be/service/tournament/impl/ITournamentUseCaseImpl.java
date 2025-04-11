package com.example.billiard_management_be.service.tournament.impl;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentData;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import com.example.billiard_management_be.entity.Tournament;
import com.example.billiard_management_be.repository.TournamentRepository;
import com.example.billiard_management_be.service.tournament.ITournamentUseCase;
import com.example.billiard_management_be.service.user.UserService;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.exceptions.NotFoundException;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Log4j2
@RequiredArgsConstructor
public class ITournamentUseCaseImpl implements ITournamentUseCase {
    private final TournamentRepository tournamentRepository;
    private final UserService userService;

    @Override
    public void create(TournamentRequest request) throws BadRequestException {
        validateOrganizer();
        Tournament tournament = ModelMapperUtils.mapper(request, Tournament.class);
        if (Objects.nonNull(request.getStartedDate()) && Objects.nonNull(request.getEndedDate())) {
            if (request.getStartedDate().isAfter(request.getEndedDate())) {
                throw new BadRequestException(ExceptionMessage.TOURNAMENT_DATE_INVALID);
            }
        }
        tournamentRepository.save(tournament);
    }

    @Override
    public void update(Integer id, TournamentRequest request) throws BadRequestException {
        validateOrganizer();
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_NOT_FOUND));
        ModelMapperUtils.mapIfNotNull(request, tournament);
        if (Objects.nonNull(request.getStartedDate()) && Objects.nonNull(request.getEndedDate())) {
            if (request.getStartedDate().isAfter(request.getEndedDate())) {
                throw new BadRequestException(ExceptionMessage.TOURNAMENT_DATE_INVALID);
            }
        }
        tournamentRepository.save(tournament);
    }


    @Override
    public void delete(Integer id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_NOT_FOUND));
        tournament.setIsDeleted(true);
        tournamentRepository.save(tournament);
    }

    @Override
    public TournamentResponse getAllByCondition(PageableRequest pageableRequest) {
        Page<Tournament> page = tournamentRepository.findAllByCondition(pageableRequest.getPageable());
        return TournamentResponse.builder()
                .count(page.getContent().size())
                .total(page.getTotalElements())
                .data(ModelMapperUtils.mapList(page.getContent(), TournamentData.class))
                .build();
    }


    private void validateOrganizer() throws BadRequestException {
        if (!userService.isOrganizer()) {
            throw new BadRequestException(ExceptionMessage.USER_NOT_IS_TOURNAMENT);
        }
    }
}
