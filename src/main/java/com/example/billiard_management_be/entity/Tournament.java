package com.example.billiard_management_be.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tournaments")
@AllArgsConstructor
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "competition_format")
    private String competitionFormat;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "amount_limit")
    private Integer amountLimit;

    @Column(name = "award")
    private Double award;

    @Column(name = "sponsor")
    private String sponsor;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "ended_date")
    private LocalDateTime endedDate;

    @Column(name = "created_date", insertable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", insertable = false, updatable = false)
    private LocalDateTime updatedDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted", insertable = false)
    private Boolean isDeleted;

    @Column(name = "success_amount")
    private Integer successAmount;

    @Column(name = "cancel_amount")
    private Integer cancelAmount;

}