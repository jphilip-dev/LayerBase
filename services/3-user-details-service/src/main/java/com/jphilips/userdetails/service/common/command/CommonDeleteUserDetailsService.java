package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.redis.constant.CacheKeys;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.userdetails.dto.cqrs.DeleteUserDetailsCommand;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDeleteUserDetailsService implements Command<DeleteUserDetailsCommand, Void> {

    private final UserDetailsManager userDetailsManager;
    private final RedisHelper redisHelper;

    @Override
    @CacheEvict(value = CacheKeys.UserDetails.USER_DETAILS_BY_ID, key = "#command.userDetailsId()")
    public Void execute(DeleteUserDetailsCommand command) {

        var userDetails = userDetailsManager.validateById(command.userDetailsId());

        // clear page cache
        redisHelper.evictByTag(CacheKeys.UserDetails.USER_DETAILS_TAG);

        userDetailsManager.delete(userDetails);

        return null;
    }
}
