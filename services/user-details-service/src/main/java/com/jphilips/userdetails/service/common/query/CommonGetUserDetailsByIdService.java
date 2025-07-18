package com.jphilips.userdetails.service.common.query;

import com.jphilips.shared.domain.util.Query;
import com.jphilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import com.jphilips.userdetails.dto.cqrs.GetUserDetailsByIdQuery;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonGetUserDetailsByIdService implements Query<GetUserDetailsByIdQuery, UserDetailsResponseDto> {

    private final UserDetailsMapper userDetailsMapper;
    private final UserDetailsManager userDetailsManager;

    @Override
    @Cacheable(value = CacheKeys.UserDetails.USER_DETAILS_BY_ID, key = "#query.userDetailsId()")
    public UserDetailsResponseDto execute(GetUserDetailsByIdQuery query) {

        var details = userDetailsManager.validateById(query.userDetailsId());

        return userDetailsMapper.toDto(details);
    }
}
