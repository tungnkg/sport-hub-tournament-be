package com.example.billiard_management_be.service.tournament_register.impl;

import com.example.billiard_management_be.dto.request.PageableRequest;
import com.example.billiard_management_be.service.auth.dto.PlayerInfo;
import com.example.billiard_management_be.service.tournament_register.dto.FindTournamentRegisterRequest;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterData;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterRequest;
import com.example.billiard_management_be.service.tournament.dto.TournamentResponse;
import com.example.billiard_management_be.entity.Tournament;
import com.example.billiard_management_be.entity.TournamentRegister;
import com.example.billiard_management_be.entity.User;
import com.example.billiard_management_be.repository.TournamentRegisterRepository;
import com.example.billiard_management_be.repository.TournamentRepository;
import com.example.billiard_management_be.repository.UserRepository;
import com.example.billiard_management_be.service.tournament_register.ITournamentRegisterUseCase;
import com.example.billiard_management_be.service.tournament_register.dto.TournamentRegisterResponse;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.exceptions.NotFoundException;
import com.example.billiard_management_be.shared.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ITournamentRegisterUseCaseImpl implements ITournamentRegisterUseCase {
    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final TournamentRegisterRepository tournamentRegisterRepository;

    @Override
    @Transactional
    public void register(TournamentRegisterRequest request) {
        Tournament tournament = tournamentRepository.findById(request.getTournamentId())
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_NOT_FOUND));
        User user = userRepository.findById(request.getPlayerId())
                .filter(i -> !i.getIsDeleted())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if(tournamentRegisterRepository.existsByPlayerIdAndTournamentId(user.getId(), tournament.getId())) {
            throw new NotFoundException(ExceptionMessage.TOURNAMENT_REGISTERED);
        }
        if(tournament.getSuccessAmount() >= tournament.getAmountLimit()) {
            throw new NotFoundException(ExceptionMessage.TOURNAMENT_FULL);
        }
        TournamentRegister tournamentRegister =  TournamentRegister.builder()
                .playerId(user.getId())
                .tournamentId(tournament.getId())
                .paymentFeeDate(request.getPaymentFeeDate())
                .paymentMethod(request.getPaymentMethod())
                .isPaymentFee(request.getIsPaymentFee())
                .build();
        tournamentRegisterRepository.save(tournamentRegister);
        tournament.setSuccessAmount(tournament.getSuccessAmount() + 1);
        tournamentRepository.save(tournament);
    }

    @Override
    @Transactional
    public void cancel(Integer id) {
        TournamentRegister tournamentRegister = tournamentRegisterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_REGISTER_NOT_FOUND));
        tournamentRegister.setStatus(false);
        Tournament tournament = tournamentRepository.findById(tournamentRegister.getTournamentId())
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.TOURNAMENT_NOT_FOUND));
        tournament.setSuccessAmount(tournament.getSuccessAmount() - 1);
        tournamentRegisterRepository.save(tournamentRegister);
        tournamentRepository.save(tournament);
    }


    @Override
    public TournamentRegisterResponse getAllByCondition(FindTournamentRegisterRequest request) {
        Page<TournamentRegister> tournamentRegisterPage = tournamentRegisterRepository.findAllByCondition(request, request.getPageable());
        List<TournamentRegisterData> list = tournamentRegisterPage.getContent().stream()
                .map(i -> {
                    TournamentRegisterData tournamentRegisterData = ModelMapperUtils.mapper(i, TournamentRegisterData.class);
                    User user = userRepository.findById(tournamentRegisterData.getPlayerId())
                            .filter(j -> !j.getIsDeleted())
                            .orElseThrow(() -> new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
                    PlayerInfo playerInfo = ModelMapperUtils.mapper(user, PlayerInfo.class);
                    tournamentRegisterData.setPlayerInfo(playerInfo);
                    return tournamentRegisterData;
                })
                .toList();
        return TournamentRegisterResponse.builder()
                .count(tournamentRegisterPage.getNumberOfElements())
                .total(tournamentRegisterPage.getTotalElements())
                .data(list)
                .build();
    }

}
