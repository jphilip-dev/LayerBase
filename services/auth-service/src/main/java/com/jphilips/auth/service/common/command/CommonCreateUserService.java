package com.jphilips.auth.service.common.command;

import com.jphiilips.shared.domain.exception.custom.AppException;
import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.auth.config.UserDetailsClient;
import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.CreateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphiilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCreateUserService implements Command<CreateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final RoleSeeder roleSeeder;

    private final FeignCaller feignCaller;
    private final UserDetailsClient userDetailsClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto execute(CreateUserCommand command) {

        // Extract Payload
        var userRequestDto = command.userRequestDto();

        // Convert to entity
        var newUser = authMapper.toEntity(userRequestDto);
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        // Set Status
        newUser.setIsActive(true);

        // Add Role
        newUser.addRole(roleSeeder.getDefaultRole());

        // Save user, manager logs the save
        var savedUser = authManager.save(newUser);

        // logging
        MDC.put("userId", savedUser.getId().toString());
        log.info("New User Registered: {} with id: {}", savedUser.getEmail(), savedUser.getId());

        // Create UserDetails Request Dto
        var userDetailsRequestDto = UserDetailsRequestDto.builder()
                .id(savedUser.getId())
                .name(userRequestDto.getName())
                .address(userRequestDto.getAddress())
                .birthDate(userRequestDto.getBirthDate())
                .build();

        try {
            log.info("Creating User Details - Internal Call");
            // Rest call using feign S2S
            var response = feignCaller.callWithErrorHandling(
                    UserDetailsClient.class.getSimpleName(),
                    () -> userDetailsClient.createUserDetails(userDetailsRequestDto));

        } catch (AppException exception) {

            log.warn("Error creating user details, reverting AuthDetails creation");
            authManager.delete(savedUser);
            // Re throw exception
            throw exception;
        }

        // Convert and return
        return authMapper.toDto(savedUser);
    }
}
