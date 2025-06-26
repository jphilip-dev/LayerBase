package com.jphilips.auth.controller.admin;

import com.jphilips.auth.dto.AdminUpdateUserRequestDto;
import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.service.admin.command.AdminUpdateUserService;
import com.jphilips.auth.service.common.command.CommonDeleteUserService;
import com.jphilips.shared.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminCommandController {

    private final AdminUpdateUserService adminUpdateUserService;
    private final CommonDeleteUserService commonDeleteUserService;

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(
            @PathVariable Long id,
            @Valid
            @RequestBody AdminUpdateUserRequestDto requestDto) {

        var command = UpdateUserCommand.builder()
                .userId(id)
                .userRequestDto(requestDto.getUser())
                .isActive(requestDto.getIsActive())
                .isAdmin(requestDto.getIsAdmin())
                .build();

        var response = adminUpdateUserService.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> deleteUserById(
            @PathVariable Long id) {

        var command = DeleteUserCommand.builder()
                .userId(id)
                .build();

        commonDeleteUserService.execute(command);

        return ResponseEntity.noContent().build();
    }
}
