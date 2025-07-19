package com.jphilips.auth.controller.user;

import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.dto.cqrs.query.GetUserByIdQuery;
import com.jphilips.auth.service.user.query.GetUserByEmailService;
import com.jphilips.auth.service.user.query.GetUserByIdService;
import com.jphilips.shared.domain.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final GetUserByIdService getUserByIdService;
    private final GetUserByEmailService getUserByEmailService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @PathVariable Long id) {

        var query = GetUserByIdQuery.builder()
                .headerUserId(headerUserId)
                .userId(id)
                .build();

        var response = getUserByIdService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping()
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @RequestParam String email) {

        var query = GetUserByEmailQuery.builder()
                .headerUserId(headerUserId)
                .email(email)
                .build();

        var response = getUserByEmailService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
