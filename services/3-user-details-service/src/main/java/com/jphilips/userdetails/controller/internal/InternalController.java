package com.jphilips.userdetails.controller.internal;

import com.jphilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.CreateUserDetailsCommand;
import com.jphilips.userdetails.dto.cqrs.DeleteUserDetailsCommand;
import com.jphilips.userdetails.dto.cqrs.GetUserDetailsByIdQuery;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.service.common.command.CommonCreateUserDetailsService;
import com.jphilips.userdetails.service.common.command.CommonDeleteUserDetailsService;
import com.jphilips.userdetails.service.common.command.CommonUpdateUserDetailsService;
import com.jphilips.userdetails.service.common.query.CommonGetUserDetailsByIdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/user-details")
@RequiredArgsConstructor
public class InternalController {

    private final CommonCreateUserDetailsService commonCreateUserDetailsService;
    private final CommonGetUserDetailsByIdService commonGetUserDetailsByIdService;
    private final CommonUpdateUserDetailsService commonUpdateUserDetailsService;
    private final CommonDeleteUserDetailsService commonDeleteUserDetailsService;

    @PostMapping
    public ResponseEntity<UserDetailsResponseDto> createUserDetails(
            @Valid
            @RequestBody UserDetailsRequestDto userDetailsRequestDto) {

        var command = CreateUserDetailsCommand.builder()
                .userDetailsRequestDto(userDetailsRequestDto)
                .build();

        var response = commonCreateUserDetailsService.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> updateUserDetails(
            @PathVariable Long id,
            @Valid
            @RequestBody UserDetailsRequestDto userDetailsRequestDto) {

        var command = UpdateUserDetailsCommand.builder()
                .userDetailsId(id)
                .userDetailsRequestDto(userDetailsRequestDto)
                .build();

        var response = commonUpdateUserDetailsService.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> deleteUserDetails(
            @PathVariable Long id) {

        var command = DeleteUserDetailsCommand.builder()
                .userDetailsId(id)
                .build();

        commonDeleteUserDetailsService.execute(command);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> getUserDetailsById(
            @PathVariable Long id){

        var query = GetUserDetailsByIdQuery.builder()
                .userDetailsId(id)
                .build();

        var response = commonGetUserDetailsByIdService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
