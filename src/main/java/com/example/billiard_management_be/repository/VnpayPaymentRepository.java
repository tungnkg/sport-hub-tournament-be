package com.example.billiard_management_be.repository;

import com.example.billiard_management_be.entity.VnpayPayment;
import com.example.billiard_management_be.service.vnpay.dto.FindVNPayPaymentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VnpayPaymentRepository extends JpaRepository<VnpayPayment, Integer> {
    Page<VnpayPayment> findAllByFilter(FindVNPayPaymentRequest request, Pageable pageable);
}