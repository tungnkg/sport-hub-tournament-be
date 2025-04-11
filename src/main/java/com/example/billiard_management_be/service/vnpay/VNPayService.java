package com.example.billiard_management_be.service.vnpay;

import com.example.billiard_management_be.entity.VnpayPayment;
import com.example.billiard_management_be.repository.VnpayPaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VNPayService {
    @Value("${vnpay.payment.url}")
    private String vnp_PayUrl;
    @Value("${vnpay.return_url}")
    private String vnp_ReturnUrl;
    @Value("${vnpay.tmn_code}")
    private String vnp_TmnCode ;
    @Value("${vnpay.secret_key}")
    private String secretKey;
    @Value("${vnpay.version}")
    private String vnp_Version;
    @Value("${vnpay.command}")
    private String vnp_Command;
    @Value("${vnpay.order_type}")
    private String orderType;
    private final VnpayPaymentRepository vnpayPaymentRepository;
    private final int EXPIRED_TIME = 15; // minutes

    public VNPayUrlResponse createVnPayPayment(PaymentRequest request, HttpServletRequest httpRequest) throws UnsupportedEncodingException, BadRequestException {

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_Amount", String.valueOf(request.getAmount()));

        if (request.getBankCode() != null && !request.getBankCode().isEmpty()) {
            vnp_Params.put("vnp_BankCode", request.getBankCode());
        }

        if(request.getLanguage() != null && !request.getLanguage().isEmpty()) {
            vnp_Params.put("vnp_Locale", request.getLanguage());
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }

        String txnRefPending = VNPayUtils.getRandomNumber(8);

        vnp_Params.put("vnp_TxnRef", txnRefPending);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + txnRefPending);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", VNPayUtils.getIpAddress(httpRequest));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // đảm bảo formatter cũng đúng timezone

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);


        validatePreviousBuildPayment(request.getOrderId(), request.getOrderType());
        VnpayPayment vnpayPayment = VnpayPayment.builder()
                .orderId(request.getOrderId())
                .amount(String.valueOf(request.getAmount()))
                .bankCode(request.getBankCode())
                .language(Objects.nonNull(request.getLanguage()) && !request.getLanguage().isEmpty()? request.getLanguage() : "vn")
                .userId(UserUtils.getUserId())
                .orderType(request.getOrderType())
                .status(Constants.VNPAYPaymentStatus.PENDING)
                .txnRef(txnRefPending)
                .build();
        vnpayPayment = this.vnpayPaymentRepository.save(vnpayPayment);
        saveToCache(vnpayPayment.getId());
        return VNPayUrlResponse.builder()
                .url(vnp_PayUrl + "?" + buildQueryUrl(vnp_Params))
                .transactionId(vnpayPayment.getId())
                .build();
    }

    public CompletedPaymentResponse paymentCallBack(CompletedPaymentRequest request){
        VnpayPayment vnpayPayment = this.vnpayPaymentRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("payment_not_found", null, Locale.getDefault())));

        vnpayPayment.setBankCode(request.getBankCode());
        vnpayPayment.setBankTransactionNo(request.getBankTransactionNo());
        vnpayPayment.setCardType(request.getCardType());
        vnpayPayment.setOrderInfo(request.getOrderInfo());
        vnpayPayment.setPayDate(request.getPayDate());
        vnpayPayment.setStatus(request.getResponseCode().equals("00") ? Constants.VNPAYPaymentStatus.SUCCESS : Constants.VNPAYPaymentStatus.FAIL);
        vnpayPayment.setTransactionNo(request.getTransactionNo());
        vnpayPayment.setTxnRef(request.getTxnRef());

        vnpayPayment = this.vnpayPaymentRepository.save(vnpayPayment);

        if(vnpayPayment.getStatus().equals(Constants.VNPAYPaymentStatus.SUCCESS)) {
            if (vnpayPayment.getOrderType() == Constants.ORDERTYPE.COURSE || vnpayPayment.getOrderType() == Constants.ORDERTYPE.CERTIFICATE) {
                this.moocCourseStudentRegisterRepository.save(MoocCourseStudentRegister.builder()
                        .course(this.moocCourseRepository.findById(vnpayPayment.getOrderId())
                                .filter(i -> !i.getIsDeleted())
                                .orElseThrow(() -> new NotFoundException(messageSource.getMessage("course_not_found", null, Locale.getDefault()))))
                        .studentUserId(vnpayPayment.getUserId())
                        .isScheduledByTeacher(Boolean.TRUE)
                        .createdDate(Instant.now())
                        .build());
            }
        }


        return CompletedPaymentResponse.builder()
                .orderId(vnpayPayment.getOrderId())
                .orderType(vnpayPayment.getOrderType())
                .userId(vnpayPayment.getUserId())
                .transactionId(vnpayPayment.getId())
                .isSuccess(vnpayPayment.getStatus().equals(Constants.VNPAYPaymentStatus.SUCCESS))
                .build();
    }


    private String buildQueryUrl(Map<String, String> vnp_Params) throws UnsupportedEncodingException {
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayUtils.hmacSHA512(secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return query.toString();
    }

    private void validatePreviousBuildPayment(Integer orderId, Integer orderType) throws BadRequestException {
        if (orderType == Constants.ORDERTYPE.COURSE || orderType == Constants.ORDERTYPE.CERTIFICATE) {
            this.moocCourseRepository.findById(orderId)
                    .filter(i -> !i.getIsDeleted())
                    .orElseThrow(() -> new NotFoundException(messageSource.getMessage("course_not_found", null, Locale.getDefault())));
        }
        if (orderType == Constants.ORDERTYPE.EXAM) {
            this.moocCourseUnitRepository.findById(orderId)
                    .filter(i -> !i.getIsDeleted())
                    .orElseThrow(() -> new NotFoundException(messageSource.getMessage("mooc_course_unit_not_found", null, Locale.getDefault())));
        }


        if(this.vnpayPaymentRepository.existsByOrderIdAndOrderTypeAndUserIdAndStatus(orderId, orderType,
                UserUtils.getUserId(),
                Constants.VNPAYPaymentStatus.SUCCESS)) {
            throw new BadRequestException(messageSource.getMessage("payment_is_exists", null, Locale.getDefault()));
        }
    }


    public Page<VNPayPaymentData> getAllByFilter(FindVNPayPaymentRequest request) {
        Page<VnpayPayment> page = vnpayPaymentRepository.findAllByFilter(request, request.getPageable());
        List<VNPayPaymentData> vnPayPaymentDataList = ModelMapperUtils.mapList(page.getContent(), VNPayPaymentData.class);
        enrichVNPayPaymentData(vnPayPaymentDataList);
        return new PageImpl<>(vnPayPaymentDataList, page.getPageable(), page.getTotalElements());
    }

    private void enrichVNPayPaymentData(List<VNPayPaymentData> vnPayPaymentDataList) {
        for (VNPayPaymentData vnPayPaymentData : vnPayPaymentDataList) {
            if(vnPayPaymentData.getOrderType().equals(Constants.ORDERTYPE.COURSE) || vnPayPaymentData.getOrderType().equals(Constants.ORDERTYPE.CERTIFICATE)) {
                vnPayPaymentData.setOrderName(moocCourseRepository.findById(vnPayPaymentData.getOrderId())
                        .filter(i -> !i.getIsDeleted())
                        .map(MoocCourse::getName)
                        .orElseThrow(() -> new NotFoundException(messageSource.getMessage("course_not_found", null, Locale.getDefault()))));

            }else{
                vnPayPaymentData.setOrderName(moocCourseUnitRepository.findById(vnPayPaymentData.getOrderId())
                        .filter(i -> !i.getIsDeleted())
                        .map(MoocCourseUnit::getName)
                        .orElseThrow(() -> new NotFoundException(messageSource.getMessage("mooc_course_unit_not_found", null, Locale.getDefault()))));
            }
            AuthUserProfile authUserProfile = authUserProfileRepository.findFirstByUserId(Long.valueOf(vnPayPaymentData.getUserId()))
                    .orElseThrow(() -> new NotFoundException(messageSource.getMessage("cannot_find_user", null, Locale.getDefault())));
            vnPayPaymentData.setUserName(authUserProfile.getName());
        }
    }

    public CompletedPaymentRequest callBack(HttpServletRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter( "vnp_PayDate"), formatter);
        return CompletedPaymentRequest.builder()
                .amount(Long.valueOf(request.getParameter("vnp_Amount")))
                .bankCode(request.getParameter("vnp_BankCode"))
                .bankTransactionNo(request.getParameter("vnp_BankTranNo"))
                .cardType(request.getParameter("vnp_CardType"))
                .orderInfo(request.getParameter("vnp_OrderInfo"))
                .payDate(localDateTime.toInstant(ZoneOffset.UTC))
                .responseCode(request.getParameter("vnp_ResponseCode"))
                .transactionNo(request.getParameter("vnp_TransactionNo"))
                .transactionStatus(request.getParameter("vnp_TransactionStatus"))
                .txnRef(request.getParameter("vnp_TxnRef"))
                .build();
    }

    public void saveToCache(Integer vnpayTrancactionId) {
        String key = "transaction_id:" + vnpayTrancactionId;
        CacheUtils.setIfNotExists(key, vnpayTrancactionId, 15, TimeUnit.MINUTES);
    }

    public void checkExpiredPayments() {
        Iterable<String> keys = CacheUtils.getKeysByPattern("transaction_id:*");
        for (String key : keys) {
            Integer transactionId = CacheUtils.get(key);
            if (transactionId != null) {
                VnpayPayment vnpayPayment = vnpayPaymentRepository.findById(transactionId).orElse(null);
                if (vnpayPayment != null) {
                    Duration duration = Duration.between(vnpayPayment.getCreatedDate(), Instant.now());
                    if (duration.toMinutes() >= EXPIRED_TIME) {
                        vnpayPayment.setStatus(Constants.VNPAYPaymentStatus.TIMEOUT);
                        CacheUtils.getAndDelete(key);
                    }
                }

            }
        }
    }
}