package com.jphilips.auth.service.common.query;

import com.jphilips.auth.entity.User;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.util.Query;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonGetUserByEmailService implements Query<GetUserByEmailQuery, UserResponseDto> {

    private final AuthManager authManager;
    private final AuthMapper authMapper;

    @Override
    @Cacheable(key = CacheKeys.Auth.USER_BY_EMAIL, value = "#query.email()")
    public UserResponseDto execute(GetUserByEmailQuery query) {

        var user = authManager.validateUser(query.email());

        return  execute(user);
    }


    public UserResponseDto execute(User user) {
        return authMapper.toDto(user);
    }

}
