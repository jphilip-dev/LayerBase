package com.jphilips.auth.service.common.command;

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

        // Extract Payload
        var userId = command.userId();
        var userRequestDto = command.userRequestDto();

        // Retrieve user
        var savedUser = authManager.validateUser(userId);

        // check if email change, then validate it
        if(savedUser.getEmail().equalsIgnoreCase(userRequestDto.getEmail())){
            authManager.checkEmailAvailability(userRequestDto.getEmail());
        }

        // Change password
        savedUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        // TODO: Call user profile to update name, address and birthdate

        authManager.save(savedUser);

        return authMapper.toDto(savedUser);
    }
}
