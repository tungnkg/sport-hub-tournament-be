package com.example.billiard_management_be.service.vnpay;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompletedPaymentResponse {
    @NotNull
    private Integer transactionId;
    private Integer orderId;
    private Integer orderType;
    private Integer userId;
    private Boolean isSuccess;
}
