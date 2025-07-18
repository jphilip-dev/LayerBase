package com.jphilips.email.service;

import com.jphilips.email.dto.EmailContextDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailContextDto context) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(context.getToEmail());
        message.setSubject(context.getSubject());
        message.setText(context.getBody());
        log.info("Sending OTP to: {}", context.getToEmail());
        mailSender.send(message);
        log.info("OTP sent to: {}", context.getToEmail());
    }
}
