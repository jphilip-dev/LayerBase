package com.jphilips.auth.service.common.command;

import com.jphilips.auth.dto.cqrs.command.RequestOtpCommand;
import com.jphilips.auth.service.auth.command.RequestOtpService;
import com.jphilips.shared.domain.dto.kafka.payload.UserRegisteredPayload;
import com.jphilips.shared.domain.enums.EventType;
import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.CreateUserCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.exception.custom.InternalCallException;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import com.jphilips.shared.spring.kafka.service.EventPublisher;
import com.jphilips.shared.spring.kafka.util.KafkaTopics;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCreateUserService implements Command<CreateUserCommand, UserResponseDto> {

    private final AuthMapper authMapper;
    private final AuthManager authManager;
    private final RoleSeeder roleSeeder;
    private final EventPublisher eventPublisher;
    private final KafkaTopics kafkaTopics;

    private final RedisHelper redisHelper;
    private final RequestOtpService requestOtpService;

    private final FeignCaller feignCaller;
    private final UserDetailsClient userDetailsClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CachePut(value = CacheKeys.Auth.USER_BY_ID, key = "#result.id")
    public UserResponseDto execute(CreateUserCommand command) {

        // Extract Payload
        var userRequestDto = command.userRequestDto();

        // Convert to entity
        var newUser = authMapper.toEntity(userRequestDto);
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        // Set Status
        newUser.setIsActive(false);

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

        } catch (AppException | InternalCallException exception) {

            log.warn("Error creating user details, reverting AuthDetails creation");
            authManager.delete(savedUser);
            // Re throw exception
            throw exception;
        }

        // Send otp for activation
        requestOtpService.execute(new RequestOtpCommand(savedUser.getEmail()));

        // analytics
        var payload = UserRegisteredPayload.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .name(userDetailsRequestDto.getName())
                .build();

        eventPublisher.publish(EventType.USER_REGISTERED, payload, kafkaTopics.getAuthEvent(), MDC.get("requestId"));

        // add to user by mail cache then clear page cache
        redisHelper.put(CacheKeys.Auth.USER_BY_EMAIL + savedUser.getEmail(), savedUser);
        redisHelper.evictByTag(CacheKeys.Auth.USER_PAGE_TAG);


        // Convert and return
        return authMapper.toDto(savedUser);
    }
}
