package com.jphilips.userdetails.service.common.query;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jphilips.shared.domain.util.Query;
import com.jphilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import com.jphilips.shared.spring.redis.util.CacheKeys;
import com.jphilips.shared.spring.util.PagedResponse;
import com.jphilips.userdetails.dto.cqrs.GetAllUserDetailsQuery;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.entity.UserDetails;
import com.jphilips.userdetails.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonGetAllUserDetailsService implements Query<GetAllUserDetailsQuery, PagedResponse<UserDetailsResponseDto>> {

    private final UserDetailsRepository userDetailsRepository;
    private final UserDetailsMapper userDetailsMapper;

    private final RedisHelper redisHelper;

    @Override
    public PagedResponse<UserDetailsResponseDto> execute(GetAllUserDetailsQuery query) {

        String cacheKey = CacheKeys.UserDetails.USER_DETAILS_PAGE + query.pageable().toString();

        var cached = redisHelper.get(
                cacheKey,
                new TypeReference<PagedResponse<UserDetailsResponseDto>>() {}
        );

        if (cached != null) {
            log.info("loaded cached");
            return cached;
        }

        Page<UserDetails> userDetailsPage = userDetailsRepository.findAll(query.pageable());

        List<UserDetailsResponseDto> content = userDetailsPage.getContent().stream()
                .map(userDetailsMapper::toDto)
                .toList();

        var response = new PagedResponse<>(content,userDetailsPage);

        redisHelper.put(cacheKey, response, CacheKeys.Auth.USER_PAGE_TAG);

        return response;
    }
}
