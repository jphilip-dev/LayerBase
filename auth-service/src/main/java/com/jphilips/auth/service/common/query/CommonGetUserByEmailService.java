package com.jphilips.auth.service.common.query;

import com.jphilips.auth.entity.User;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonGetUserByEmailService implements Query<GetUserByEmailQuery, UserResponseDto> {

    private final AuthManager authManager;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto execute(GetUserByEmailQuery query) {

        var user = authManager.validateUser(query.email());

        return  execute(user);
    }

    public UserResponseDto execute(User user) {
        return authMapper.toDto(user);
    }

}
