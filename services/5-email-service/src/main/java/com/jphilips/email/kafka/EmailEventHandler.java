package com.jphilips.email.kafka;

import com.jphilips.email.dto.EmailContextDto;
import com.jphilips.email.service.EmailService;
import com.jphilips.shared.spring.kafka.dto.AppEvent;
import com.jphilips.shared.spring.kafka.dto.payload.EmailOtpPayload;
import com.jphilips.shared.spring.util.ObjectMapperHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailEventHandler {
    private final ObjectMapperHandler objectMapperHandler;
    private final EmailService emailService;

    public void handleEmailOtp(AppEvent<?> rawEvent){

        EmailOtpPayload payload = objectMapperHandler.convert(rawEvent.getPayload(), EmailOtpPayload.class);

        try{
            var contextDto =  EmailContextDto.builder()
                    .toEmail(payload.getEmail())
                    .subject("Your OTP Code")
                    .body("Your OTP is: " + payload.getOtp() + "\nThis code will expire in 5 minutes.")
                    .build();
            emailService.sendEmail(contextDto);
        }catch (Exception ex){
            log.error("Email failed: {}", ex.getMessage());
        }

    }
}
