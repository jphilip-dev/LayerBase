package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.CreateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonCreateUserService implements Command<CreateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;

    private final RoleSeeder roleSeeder;

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

        // TODO: call user service to create the profile

        // Convert and return
        return authMapper.toDto(savedUser);
    }
}
