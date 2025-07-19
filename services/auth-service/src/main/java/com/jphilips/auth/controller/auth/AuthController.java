package com.jphilips.auth.controller.auth;

import com.jphilips.auth.dto.LoginRequestDto;
import com.jphilips.auth.dto.TokenResponseDto;
import com.jphilips.auth.dto.UserRequestDto;
import com.jphilips.auth.dto.cqrs.command.*;
import com.jphilips.auth.service.auth.command.ActivationService;
import com.jphilips.auth.service.auth.command.AuthenticateService;
import com.jphilips.auth.service.auth.command.RequestOtpService;
import com.jphilips.auth.service.auth.command.ValidateTokenService;
import com.jphilips.auth.service.common.command.CommonCreateUserService;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.shared.domain.validator.groups.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CommonCreateUserService commonCreateUserService;
    private final AuthenticateService authenticateService;
    private final ValidateTokenService validateTokenService;

    private final ActivationService activationService;
    private final RequestOtpService requestOtpService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @Validated({Default.class, OnCreate.class})
            @RequestBody UserRequestDto userRequestDto) {

        var response = commonCreateUserService.execute(new CreateUserCommand(userRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        var command = AuthenticateCommand.builder()
                .loginRequestDto(loginRequestDto)
                .build();

        var response = authenticateService.execute(command);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @GetMapping("/validate")
    public ResponseEntity<UserResponseDto> validateToken(
            @RequestHeader(value = "Authorization", required = false)
            String token) {

        var command = ValidateTokenCommand.builder()
                .token(token)
                .build();

        var response = validateTokenService.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/request-otp/{email}")
    public ResponseEntity<UserResponseDto> requestOtp(@PathVariable String email) {

        var command = RequestOtpCommand.builder()
                .toEmail(email)
                .build();

        var response = requestOtpService.execute(command);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activate/{email}")
    public ResponseEntity<UserResponseDto> activateAccount(
            @PathVariable String email,
            @RequestParam String otp
    ) {

        var command = ActivationCommand.builder()
                .email(email)
                .otp(otp)
                .build();

        var response = activationService.execute(command);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
