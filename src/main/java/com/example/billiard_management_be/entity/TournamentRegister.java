package com.example.billiard_management_be.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tournament_register")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TournamentRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "is_payment_fee")
    private Boolean isPaymentFee;

    @Column(name = "payment_fee_date")
    private Instant paymentFeeDate;

    @Column(name = "created_date", insertable = false,updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date",  insertable = false,updatable = false)
    private LocalDateTime updatedDate;

    @Column(name = "tournament_id")
    private Integer tournamentId;

    @Column(name = "status", insertable = false)
    private Boolean status;

    @Column(name = "award")
    private Integer award;

    @Column(name = "payment_method")
    private String paymentMethod;

}