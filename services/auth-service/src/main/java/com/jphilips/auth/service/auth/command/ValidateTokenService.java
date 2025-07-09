package com.jphilips.auth.service.auth.command;

import com.jphilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.auth.dto.cqrs.command.ValidateTokenCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.exceptions.custom.MissingJwtException;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.util.JwtUtil;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.shared.domain.util.Command;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateTokenService implements Command<ValidateTokenCommand, UserResponseDto> {

    private final JwtUtil jwtUtil;
    private final AuthManager  authManager;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto execute(ValidateTokenCommand command) {

        // Extract
        var token = command.token();

        // Validate presence and format
        if (token == null || !token.startsWith("Bearer ")) {
            throw new MissingJwtException(AuthErrorCode.JWT_MISSING);
        }

        // Validate and parse claims (JwtException handled by ControllerAdvice)
        var claims = jwtUtil.validateToken(token.substring(7));

        // Extract custom claims
        Long id = claims.get("id", Long.class);

        var user = authManager.validateUser(id);

        // Inactive Check
        authManager.validateStatus(user);

        // Parse and return the AuthDetailsDto
        return authMapper.toDto(user, MDC.get("requestId"));
    }
}
