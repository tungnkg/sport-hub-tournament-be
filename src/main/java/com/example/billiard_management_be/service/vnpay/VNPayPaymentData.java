package com.example.billiard_management_be.service.vnpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VNPayPaymentData {
    private Integer id;
    private String amount;
    private String bankCode;
    private String bankTransactionNo;
    private String cardType;
    private String orderInfo;
    private Instant payDate;
    private Integer status;
    private String transactionNo;
    private String txnRef;
    private String language;
    private Integer userId;
    private Integer orderType;
    private Integer orderId;
    private Instant createdDate;
    private Instant updatedDate;
    private String orderName;
    private String userName;
}
