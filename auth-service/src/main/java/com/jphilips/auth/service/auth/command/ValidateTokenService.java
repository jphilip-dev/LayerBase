package com.jphilips.auth.service.auth.command;

import com.jphilips.auth.dto.cqrs.command.ValidateTokenCommand;
import com.jphilips.auth.exceptions.custom.MissingJwtException;
import com.jphilips.auth.util.JwtUtil;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.shared.exceptions.errorcode.AuthErrorCode;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateTokenService implements Command<ValidateTokenCommand, UserResponseDto> {

    private final JwtUtil jwtUtil;

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
        String email = claims.get("email", String.class);
        Long id = claims.get("id", Long.class);
        List<?> rawRoles = claims.get("roles", List.class);

        List<String> roles = rawRoles.stream()
                .map(Object::toString)  // convert each element to String safely
                .toList();

        // Parse and return the AuthDetailsDto
        return new UserResponseDto(id, email, null, roles);
    }
}
