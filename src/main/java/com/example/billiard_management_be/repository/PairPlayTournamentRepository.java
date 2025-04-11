package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.PairPlayTournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PairPlayTournamentRepository extends JpaRepository<PairPlayTournament, Integer> {
}