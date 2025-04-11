package com.example.billiard_management_be.controller;

import com.example.billiard_management_be.dto.response.BaseResponse;
import com.example.billiard_management_be.service.vnpay.VNPayService;
import com.example.billiard_management_be.service.vnpay.dto.*;
import com.example.billiard_management_be.shared.constants.ExceptionMessage;
import com.example.billiard_management_be.shared.factory.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/vnpay-payment")
@RequiredArgsConstructor
public class VNPayController {
     private final VNPayService vnPayService;

     @PostMapping("create-payment-url")
     public ResponseEntity<BaseResponse<VNPayUrlResponse>> createVnPayPayment(@RequestBody @Valid PaymentRequest request, HttpServletRequest httpServletRequest) throws UnsupportedEncodingException {
         return ResponseFactory.success(vnPayService.createVnPayPayment(request, httpServletRequest));
     }

    @PutMapping("completed-payment")
    public ResponseEntity<BaseResponse<String>> payCallbackHandler(@RequestBody @Valid CompletedPaymentRequest request) {
        if(!vnPayService.paymentCallBack(request)){
            return ResponseFactory.error(ExceptionMessage.VNPAY_PAYMENT_FAIL);
        }
        return ResponseFactory.success(ExceptionMessage.VNPAY_PAYMENT_SUCCESS);
    }

    @PostMapping("get-all-by-filter")
    public ResponseEntity<BaseResponse<Page<VNPayPaymentData>>> getAllByFilter(@RequestBody FindVNPayPaymentRequest request) {
        return ResponseFactory.success(vnPayService.getAllByFilter(request));
    }

    @GetMapping("callback")
    public ResponseEntity<BaseResponse<CompletedPaymentRequest>> callBack( HttpServletRequest request) {
        return ResponseFactory.success(vnPayService.callBack(request));
    }
}
