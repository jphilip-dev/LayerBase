package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.auth.entity.User;
import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphiilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonUpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final FeignCaller feignCaller;

    private final UserDetailsClient userDetailsClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {
        // Let this method just retrieve the user and delegate to the normalized one
        var user = authManager.validateUser(command.userId());
        return execute(command, user);
    }

    public UserResponseDto execute(UpdateUserCommand command, User user) {

        // Extract payload
        var userRequestDto = command.userRequestDto();

        // Check if email changed
        if (!user.getEmail().equalsIgnoreCase(userRequestDto.getEmail())) {
            authManager.checkEmailAvailability(userRequestDto.getEmail());
            user.setEmail(userRequestDto.getEmail());
        }

        // Set new Password
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        // Create UserDetails Request Dto
        var userDetailsRequestDto = UserDetailsRequestDto.builder()
                .id(user.getId())
                .name(userRequestDto.getName())
                .address(userRequestDto.getAddress())
                .birthDate(userRequestDto.getBirthDate())
                .build();

        // Rest call using feign S2S
        var response = feignCaller.callWithErrorHandling(
                UserDetailsClient.class.getSimpleName(),
                () -> userDetailsClient.updateUserDetails(user.getId(),userDetailsRequestDto));

        // Save
        authManager.save(user);

        // Convert and return
        return authMapper.toDto(user);
    }
}

