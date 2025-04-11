package com.example.billiard_management_be.service.pair;

import com.example.billiard_management_be.service.pair.dto.PairPlayTournamentRequest;
import com.example.billiard_management_be.service.pair.dto.UpdateResultPairRequest;

public interface IPairUseCase {
    void createPair(PairPlayTournamentRequest request);
    void updatePair(Integer id, PairPlayTournamentRequest request);
    void deletePair(Integer id);
    void updateResultPair(UpdateResultPairRequest request);
}
