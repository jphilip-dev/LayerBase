package com.jphilips.auth.service.common.command;

import com.jphilips.auth.entity.User;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CommonUpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {
        // Let this method just retrieve the user and delegate to the normalized one
        var user = authManager.validateUser(command.userId());
        return execute(command, user);
    }

    public UserResponseDto execute(UpdateUserCommand command, User user) {
        var dto = command.userRequestDto();

        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())) {
            authManager.checkEmailAvailability(dto.getEmail());
            user.setEmail(dto.getEmail());
        }

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // TODO: Call user profile to update name, address, and birthDate

        authManager.save(user);

        return authMapper.toDto(user);
    }
}

