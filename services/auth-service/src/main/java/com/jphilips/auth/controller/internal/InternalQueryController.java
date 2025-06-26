package com.jphilips.auth.controller.internal;

import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.dto.cqrs.query.GetUserByIdQuery;
import com.jphilips.auth.service.common.query.CommonGetUserByEmailService;
import com.jphilips.auth.service.common.query.CommonGetUserByIdService;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/auth")
@RequiredArgsConstructor
public class InternalQueryController {

    private final CommonGetUserByEmailService commonGetUserByEmailService;
    private final CommonGetUserByIdService commonGetUserByIdService;


    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        var query = GetUserByIdQuery.builder()
                .userId(id)
                .build();

        var response = commonGetUserByIdService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {

        var query = GetUserByEmailQuery.builder()
                .email(email)
                .build();

        var response = commonGetUserByEmailService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
