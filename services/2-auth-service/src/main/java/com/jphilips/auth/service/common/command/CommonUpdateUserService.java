package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.auth.entity.User;
import com.jphilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonUpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final FeignCaller feignCaller;

    private final RedisHelper redisHelper;

    private final UserDetailsClient userDetailsClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {
        // Let this method just retrieve the user and delegate to the normalized one
        var user = authManager.validateUser(command.userId());
        return execute(command, user);
    }

    @CachePut(value = CacheKeys.Auth.USER_BY_ID, key = "#result.id")
    public UserResponseDto execute(UpdateUserCommand command, User user) {

        // Extract payload
        var userRequestDto = command.userRequestDto();

        // Check if email changed
        if (!user.getEmail().equalsIgnoreCase(userRequestDto.getEmail())) {
            authManager.checkEmailAvailability(userRequestDto.getEmail());

            // remove cache
            redisHelper.delete(CacheKeys.Auth.USER_BY_EMAIL + user.getEmail());


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

        // add to user by mail cache, delete page cache
        redisHelper.put(CacheKeys.Auth.USER_BY_EMAIL + user.getEmail(), user);
        redisHelper.evictByTag(CacheKeys.Auth.USER_PAGE_TAG);

        // Convert and return
        return authMapper.toDto(user);
    }
}

