package com.jphilips.auth.service.auth.command;

import com.jphilips.shared.domain.dto.kafka.payload.UserLoggedInPayload;
import com.jphilips.shared.domain.enums.EventType;
import com.jphilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.auth.dto.TokenResponseDto;
import com.jphilips.auth.dto.cqrs.command.AuthenticateCommand;
import com.jphilips.auth.exceptions.custom.PasswordMismatchException;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.util.JwtUtil;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.kafka.service.EventPublisher;
import com.jphilips.shared.spring.kafka.util.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticateService implements Command<AuthenticateCommand, TokenResponseDto> {

    private final AuthManager authManager;
    private final EventPublisher eventPublisher;
    private final KafkaTopics kafkaTopics;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponseDto execute(AuthenticateCommand command) {

        // Extract payload
        var loginRequestDto = command.loginRequestDto();

        // Validate email
        var user = authManager.validateUser(loginRequestDto.getEmail());

        // Validate Password
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new PasswordMismatchException(AuthErrorCode.PASSWORD_MISMATCH);
        }

        // Check Account Status
        authManager.validateStatus(user);

        // set MDC
        MDC.put("userId", user.getId().toString());

        // Generate Token
        String token = jwtUtil.generateToken(user);

        // analytics
        UserLoggedInPayload payload = new UserLoggedInPayload(user.getId());
        eventPublisher.publish(EventType.USER_LOGGED_IN, payload, kafkaTopics.getAuthEvent(), MDC.get("requestId"));

        // logging
        log.info("User:{} Logged in", user.getId());

        // Return TokenResponseDto
        return new TokenResponseDto(token);
    }

}
