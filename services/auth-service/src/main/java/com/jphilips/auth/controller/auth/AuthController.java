package com.jphilips.auth.controller.auth;

import com.jphilips.auth.dto.LoginRequestDto;
import com.jphilips.auth.dto.TokenResponseDto;
import com.jphilips.auth.dto.UserRequestDto;
import com.jphilips.auth.dto.cqrs.command.AuthenticateCommand;
import com.jphilips.auth.dto.cqrs.command.CreateUserCommand;
import com.jphilips.auth.dto.cqrs.command.ValidateTokenCommand;
import com.jphilips.auth.service.auth.command.AuthenticateService;
import com.jphilips.auth.service.auth.command.ValidateTokenService;
import com.jphilips.auth.service.common.command.CommonCreateUserService;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphiilips.shared.domain.validator.groups.OnCreate;
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

}
