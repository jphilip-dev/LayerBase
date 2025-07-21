package com.jphilips.auth.service.auth.command;

import com.jphilips.auth.dto.cqrs.command.RequestOtpCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.spring.kafka.dto.payload.EmailOtpPayload;
import com.jphilips.shared.spring.kafka.enums.EventType;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.domain.util.OtpUtil;
import com.jphilips.shared.spring.kafka.service.EventPublisher;
import com.jphilips.shared.spring.kafka.config.KafkaTopics;
import com.jphilips.shared.spring.redis.constant.CacheKeys;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RequestOtpService implements Command<RequestOtpCommand, Void> {

    private final AuthManager authManager;
    private final RedisHelper redisHelper;
    private final EventPublisher eventPublisher;
    private final KafkaTopics kafkaTopics;

    @Override
    public Void execute(RequestOtpCommand command) {

        var user = authManager.validateUser(command.toEmail());

        String otp = OtpUtil.generateOtp(6);

        var payload = EmailOtpPayload.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .otp(otp)
                .build();

        redisHelper.put(CacheKeys.Auth.AUTH_OTP_BY_USER_ID + user.getId(), otp, Duration.ofMinutes(5));

        eventPublisher.publish(EventType.EMAIL_OTP, payload, kafkaTopics.getEmailEvent(), MDC.get("requestId"));

        return null;
    }
}
