package com.jphilips.auth.service.auth.command;

import com.jphiilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.auth.dto.TokenResponseDto;
import com.jphilips.auth.dto.cqrs.command.AuthenticateCommand;
import com.jphilips.auth.exceptions.custom.PasswordMismatchException;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.util.JwtUtil;
import com.jphiilips.shared.domain.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService implements Command<AuthenticateCommand, TokenResponseDto> {

    private final AuthManager authManager;

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

        // Return TokenResponseDto
        return new TokenResponseDto(token);
    }

}
