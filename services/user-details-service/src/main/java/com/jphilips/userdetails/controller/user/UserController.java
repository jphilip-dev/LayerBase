package com.jphilips.userdetails.controller.user;

import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphiilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.GetUserDetailsByIdQuery;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.service.user.command.UpdateUserDetailsService;
import com.jphilips.userdetails.service.user.query.GetUserDetailsByIdService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-details")
public class UserController {

    private final GetUserDetailsByIdService getUserDetailsByIdService;
    private final UpdateUserDetailsService updateUserDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> getUserDetailsById(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @PathVariable Long id){

        var query = GetUserDetailsByIdQuery.builder()
                .headerUserId(headerUserId)
                .userDetailsId(id)
                .build();

        var response = getUserDetailsByIdService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsResponseDto> updateUserDetails(
            @RequestHeader(value = "X-User-Id") Long headerUserId,
            @PathVariable Long id,
            @Valid
            @RequestBody UserDetailsRequestDto userDetailsRequestDto){

        var command = UpdateUserDetailsCommand.builder()
                .headerUserId(headerUserId)
                .userDetailsId(id)
                .userDetailsRequestDto(userDetailsRequestDto)
                .build();

        var response = updateUserDetailsService.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
