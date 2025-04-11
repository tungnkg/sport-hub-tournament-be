package com.example.billiard_management_be.service.vnpay;

import com.example.billiard_management_be.dto.request.PageableRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindVNPayPaymentRequest extends PageableRequest {
    private Integer status;
    private Integer orderId;
    private String orderType;
    private String bankCode;
    private String fromDate;
    private String endDate;
}
