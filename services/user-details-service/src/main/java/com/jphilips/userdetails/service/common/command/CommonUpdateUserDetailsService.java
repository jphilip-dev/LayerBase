package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonUpdateUserDetailsService implements Command<UpdateUserDetailsCommand, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final UserDetailsMapper userDetailsMapper;

    private final RedisHelper redisHelper;

    @Override
    @CachePut(value = CacheKeys.UserDetails.USER_DETAILS_BY_ID, key = "#result.id")
    public UserDetailsResponseDto execute(UpdateUserDetailsCommand command) {

        var dto = command.userDetailsRequestDto();

        var savedUserDetails = userDetailsManager.validateById(command.userDetailsId());

        savedUserDetails.setName(dto.getName());
        savedUserDetails.setAddress(dto.getAddress());
        savedUserDetails.setBirthDate(dto.getBirthDate());

        userDetailsManager.save(savedUserDetails);

        // clear page cache
        redisHelper.evictByTag(CacheKeys.UserDetails.USER_DETAILS_TAG);

        return userDetailsMapper.toDto(savedUserDetails);
    }
}
