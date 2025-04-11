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
public class PaymentRequest {
    @NotNull
    private Integer orderId;
    @NotNull
    private Integer orderType;
    @NotNull
    private Long amount;
    private String language;
    private String bankCode;
}
