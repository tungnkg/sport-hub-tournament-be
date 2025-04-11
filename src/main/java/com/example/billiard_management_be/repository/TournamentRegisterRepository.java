package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.TournamentRegister;
import com.example.billiard_management_be.service.tournament_register.dto.FindTournamentRegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

public interface TournamentRegisterRepository extends JpaRepository<TournamentRegister, Integer> {
    boolean existsByPlayerIdAndTournamentId(Integer playerId, Integer tournamentId);

    @Query(value = """
                select t.* from tournament_register t
                where t.tournament_id = :#{#request.getTournamentId()}
""", nativeQuery = true, countProjection = "t.id")
    Page<TournamentRegister> findAllByCondition(FindTournamentRegisterRequest request, Pageable pageable);
}