package com.example.billiard_management_be.service.pair.impl;

import com.example.billiard_management_be.entity.PairPlayTournament;
import com.example.billiard_management_be.entity.Tournament;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.repository.PairPlayTournamentRepository;
import com.example.billiard_management_be.repository.TournamentRepository;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.service.pair.IPairUseCase;
import com.example.billiard_management_be.service.pair.dto.PairPlayTournamentRequest;
import com.example.billiard_management_be.service.pair.dto.UpdateResultPairRequest;
import com.example.billiard_management_be.service.user.UserService;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.exceptions.InputNotValidException;
import com.example.billiard_management_be.shared.exceptions.NotFoundException;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class IPairUseCaseImpl implements IPairUseCase {
    private final PairPlayTournamentRepository pairPlayTournamentRepository;
    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final int ROUND_EXCEPTION = 0;
    @Override
    public void createPair(PairPlayTournamentRequest request) {
        validate(request);
        PairPlayTournament pair = ModelMapperUtils.mapper(request, PairPlayTournament.class);
        this.pairPlayTournamentRepository.save(pair);
    }

    @Override
    public void updatePair(Integer id, PairPlayTournamentRequest request) {
        PairPlayTournament pair = this.pairPlayTournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.PAIR_NOT_FOUND));
        validate(request);
        ModelMapperUtils.mapIfNotNull(pair, PairPlayTournament.class);
        this.pairPlayTournamentRepository.save(pair);

    }

    @Override
    public void deletePair(Integer id) {
        PairPlayTournament pair = this.pairPlayTournamentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.PAIR_NOT_FOUND));
        this.pairPlayTournamentRepository.delete(pair);
    }

    @Override
    public void updateResultPair(UpdateResultPairRequest request) {
        PairPlayTournament pair = this.pairPlayTournamentRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.PAIR_NOT_FOUND));
        if(!pair.getPlayer1().equals(request.getPlayerWinner()) && !pair.getPlayer2().equals(request.getPlayerWinner())) {
            throw new InputNotValidException(ExceptionMessage.PLAYER_WINNER_NOT_IN_PAIR);
        }
        if(request.getResult() == null || request.getResult().isEmpty()) {
            throw new InputNotValidException(ExceptionMessage.RESULT_NOT_VALID);
        }
        pair.setResult(request.getResult());
        pair.setPlayerWinner(request.getPlayerWinner());
        pair.setEndedDate(request.getEndedDate());
        this.pairPlayTournamentRepository.save(pair);
    }

    private void validate(PairPlayTournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(request.getTournamentId())
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_NOT_FOUND));
        User player1 = userRepository.findById(request.getPlayer1())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        User player2 = userRepository.findById(request.getPlayer1())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if (player1.getId().equals(player2.getId())) {
            throw new InputNotValidException(ExceptionMessage.PLAYERS_NOT_EQUAL);
        }
        if(request.getRound() <= ROUND_EXCEPTION) {
            throw new InputNotValidException( ExceptionMessage.ROUND_NOT_VALID);
        }
    }


}
