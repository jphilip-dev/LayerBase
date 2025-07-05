package com.jphilips.auth.service.auth.command;

import com.jphiilips.shared.domain.dto.kafka.payload.UserLoggedInPayload;
import com.jphiilips.shared.domain.enums.EventType;
import com.jphiilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.auth.dto.TokenResponseDto;
import com.jphilips.auth.dto.cqrs.command.AuthenticateCommand;
import com.jphilips.auth.exceptions.custom.PasswordMismatchException;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.util.JwtUtil;
import com.jphiilips.shared.domain.util.Command;
import com.jphilips.shared.spring.util.EventPublisher;
import com.jphilips.shared.spring.util.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        // Generate Token
        String token = jwtUtil.generateToken(user);

        // analytics
        UserLoggedInPayload payload = new UserLoggedInPayload(user.getId());
        eventPublisher.publish(EventType.USER_LOGGED_IN, payload, kafkaTopics.getAuthEvents());

        // Return TokenResponseDto
        return new TokenResponseDto(token);
    }

}
