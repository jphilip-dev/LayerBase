package com.jphilips.auth.service.common.query;

import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.query.GetUserByIdQuery;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonGetUserByIdService implements Query<GetUserByIdQuery, UserResponseDto> {

    private final AuthManager authManager;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto execute(GetUserByIdQuery query) {

        var user = authManager.validateUser(query.userId());

        return authMapper.toDto(user);
    }
}
