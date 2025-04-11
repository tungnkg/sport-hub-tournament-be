package com.example.billiard_management_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pair_play_tournament")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PairPlayTournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_1")
    private Integer player1;

    @Column(name = "player_2")
    private Integer player2;

    @Column(name = "player_winner")
    private Integer playerWinner;

    @Column(name = "round")
    private Integer round;

    @Column(name = "result")
    private String result;

    @Column(name = "table_play")
    private String tablePlay;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "ended_date")
    private LocalDateTime endedDate;

    @Column(name = "created_date", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", insertable = false, updatable = false)
    private LocalDateTime updatedDate;

    @Column(name = "tournament_id")
    private Integer tournamentId;
}