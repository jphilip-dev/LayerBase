package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.FeignCallerHelper;
import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.shared.dto.UserDetailsRequestDto;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.CreateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCreateUserService implements Command<CreateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final RoleSeeder roleSeeder;
    private final FeignCallerHelper feignCallerHelper;

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

        // Create UserDetails Request Dto
        var userDetailsRequestDto = UserDetailsRequestDto.builder()
                .id(savedUser.getId())
                .name(userRequestDto.getName())
                .address(userRequestDto.getAddress())
                .birthDate(userRequestDto.getBirthDate())
                .build();

        // Rest call using feign S2S
        var response = feignCallerHelper.execute(
                userDetailsClient.getClass().getSimpleName(),
                () -> userDetailsClient.createUserDetails(userDetailsRequestDto));

        // Convert and return
        return authMapper.toDto(savedUser);
    }
}

//
//// Call user service to create the profile
//        try {
//var userDetailsDto = UserDetailsRequestDto.builder()
//        .id(savedUser.getId())
//        .name(userRequestDto.getName())
//        .address(userRequestDto.getAddress())
//        .birthDate(userRequestDto.getBirthDate())
//        .build();
//
//// Rest call
//            userDetailsClient.createUserDetails(userDetailsDto);
//
//        }catch (FeignException ex){
//
//        feignErrorHandler.handle(userDetailsClient.getClass().getSimpleName(),ex.contentUTF8());
//
//        }
