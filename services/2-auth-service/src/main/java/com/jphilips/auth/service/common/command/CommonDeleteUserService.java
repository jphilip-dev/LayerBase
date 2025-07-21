package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import com.jphilips.shared.spring.redis.constant.CacheKeys;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonDeleteUserService implements Command<DeleteUserCommand, Void> {

    private final AuthManager authManager;
    private final FeignCaller feignCaller;
    private final RedisHelper redisHelper;

    private final UserDetailsClient userDetailsClient;

    @Override
    @CacheEvict(value = CacheKeys.Auth.USER_BY_ID, key = "#command.userId()", beforeInvocation = true)
    public Void execute(DeleteUserCommand command) {
        // Extract Payload
        var userId = command.userId();

        // logging
        log.info("Deleting User: {}", userId);

        var user = authManager.validateUser(userId);

        // delete other cache
        redisHelper.delete(CacheKeys.Auth.USER_BY_EMAIL + user.getEmail());
        redisHelper.evictByTag(CacheKeys.Auth.USER_PAGE_TAG);

        // delete user details using feign S2S
        feignCaller.callWithErrorHandling(
                UserDetailsClient.class.getSimpleName(),
                () -> {
                   userDetailsClient.deleteUserDetails(userId);
                   return null;
                });

        // Delete
        authManager.delete(user);

        // logging
        log.info("User: {} deleted", userId);
        log.info("End of Deleting User");

        return null;
    }
}
