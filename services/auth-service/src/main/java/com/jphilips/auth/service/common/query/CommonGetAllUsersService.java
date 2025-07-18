package com.jphilips.auth.service.common.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.query.GetAllUsersQuery;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.entity.User;
import com.jphilips.auth.repository.UserRepository;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import com.jphilips.shared.spring.util.PagedResponse;
import com.jphilips.shared.domain.util.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonGetAllUsersService implements Query<GetAllUsersQuery, PagedResponse<UserResponseDto>> {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    private final RedisHelper redisHelper;

    @Override
    public PagedResponse<UserResponseDto> execute(GetAllUsersQuery query) {

        String cacheKey = CacheKeys.Auth.USER_PAGE + query.pageable().toString();

        var cached = redisHelper.get(
                cacheKey,
                new TypeReference<PagedResponse<UserResponseDto>>() {}
        );

        if (cached != null) {
            log.info("loaded cached");
            return cached;
        }

        Page<User> userPage = userRepository.findAll(query.pageable());
        List<UserResponseDto> content = userPage.getContent().stream()
                .map(authMapper::toDto)
                .toList();

        var response = new PagedResponse<>(content, userPage);

        redisHelper.put(cacheKey, response, CacheKeys.Auth.USER_PAGE_TAG);

        return response;
    }
}
