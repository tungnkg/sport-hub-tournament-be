package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.Tournament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
    @Query(value = """
    select t.* from tournaments t
    where is_deleted = false
""", nativeQuery = true, countProjection = "t.id")
    Page<Tournament> findAllByCondition(Pageable pageable);
}