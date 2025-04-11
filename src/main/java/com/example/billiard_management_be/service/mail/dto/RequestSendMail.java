package com.example.billiard_management_be.service.mail.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class RequestSendMail {
    String to;
    String subject;
    Map<String, Object> body;
    String templateName;
}
