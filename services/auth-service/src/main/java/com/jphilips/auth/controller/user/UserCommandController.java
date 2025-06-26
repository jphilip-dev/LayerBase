package com.jphilips.auth.controller.user;

import com.jphilips.auth.dto.UserRequestDto;
import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.service.user.command.DeleteUserService;
import com.jphilips.auth.service.user.command.UpdateUserService;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @PathVariable Long id,
            @Valid
            @RequestBody UserRequestDto userRequestDto) {

        var command = UpdateUserCommand.builder()
                .headerUserId(headerUserId)
                .userId(id)
                .userRequestDto(userRequestDto)
                .build();

        var response = updateUserService.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUserById(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @PathVariable Long id) {

        var command = DeleteUserCommand.builder()
                .headerUserId(headerUserId)
                .userId(id)
                .build();

        var response = deleteUserService.execute(command);

        return ResponseEntity.noContent().build();
    }

}
