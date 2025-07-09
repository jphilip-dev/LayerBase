package com.jphilips.auth.controller.admin;

import com.jphilips.auth.dto.cqrs.query.GetAllUsersQuery;
import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.dto.cqrs.query.GetUserByIdQuery;
import com.jphilips.auth.service.common.query.CommonGetAllUsersService;
import com.jphilips.auth.service.common.query.CommonGetUserByEmailService;
import com.jphilips.auth.service.common.query.CommonGetUserByIdService;
import com.jphilips.shared.domain.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminQueryController {

    private final CommonGetAllUsersService commonGetAllUsersService;
    private final CommonGetUserByEmailService commonGetUserByEmailService;
    private final CommonGetUserByIdService commonGetUserByIdService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String email,
            Pageable pageable) {

        if (email != null) {

            var query = GetUserByEmailQuery.builder()
                    .email(email)
                    .build();

            var response = commonGetUserByEmailService.execute(query);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {

            var response = commonGetAllUsersService.execute(new GetAllUsersQuery(pageable));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }


    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        var query = GetUserByIdQuery.builder()
                .userId(id)
                .build();

        var response = commonGetUserByIdService.execute(query);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
