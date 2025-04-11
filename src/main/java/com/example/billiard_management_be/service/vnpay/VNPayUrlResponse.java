package com.example.billiard_management_be.service.vnpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VNPayUrlResponse {
    private String url;
    private Integer transactionId;
}
