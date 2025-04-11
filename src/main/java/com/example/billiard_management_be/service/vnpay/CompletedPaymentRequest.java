package com.example.billiard_management_be.service.vnpay;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompletedPaymentRequest {
    @NotNull
    private Integer transactionId;
    private Long amount;
    private String bankCode;
    private String bankTransactionNo;
    private String cardType;
    private String orderInfo;
    private Instant payDate;
    private String responseCode;
    private String transactionNo;
    private String transactionStatus;
    private String txnRef;
}
