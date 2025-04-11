package com.example.billiard_management_be.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "vnpay_payments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VnpayPayment {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "amount")
    private String amount;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_transaction_no")
    private String bankTransactionNo;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "pay_date")
    private Instant payDate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "txn_ref")
    private String txnRef;

    @Column(name = "language")
    private String language;

    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "tournament_id")
    private Integer tournamentId;

    @Column(name = "created_date", insertable = false, updatable = false)
    private Instant createdDate;

    @Column(name = "updated_date", insertable = false, updatable = false)
    private Instant updatedDate;

}