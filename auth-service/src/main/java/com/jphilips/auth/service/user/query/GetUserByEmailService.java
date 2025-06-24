package com.jphilips.auth.service.user.query;

import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.query.CommonGetUserByEmailService;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.shared.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByEmailService implements Query<GetUserByEmailQuery, UserResponseDto> {

    private final CommonGetUserByEmailService commonGetUserByEmailService ;
    private final AuthManager authManager;

    @Override
    public UserResponseDto execute(GetUserByEmailQuery query) {

        // ownership check
        authManager.checkOwnership(query.headerUserId() ,query.email());

        // exec common
        return commonGetUserByEmailService.execute(query);
    }
}
