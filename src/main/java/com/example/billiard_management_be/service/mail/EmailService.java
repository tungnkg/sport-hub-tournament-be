package com.example.billiard_management_be.service.mail;

import com.example.billiard_management_be.service.mail.dto.RequestSendMail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public void sendNewMail(RequestSendMail input){
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(input.getBody());
            String htmlBody = templateEngine.process(input.getTemplateName(), thymeleafContext);
            message.setText(htmlBody, true);
            message.setTo(input.getTo());
            message.setSubject(input.getSubject());
            message.setFrom("sporthub@example.com");
            emailSender.send(mimeMessage);
        } catch (MessagingException ignore) {
        }
    }
}
