package com.jphilips.auth.service.user.query;

import com.jphilips.auth.dto.cqrs.query.GetUserByEmailQuery;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.query.CommonGetUserByEmailService;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphiilips.shared.domain.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByEmailService implements Query<GetUserByEmailQuery, UserResponseDto> {

    private final CommonGetUserByEmailService commonGetUserByEmailService ;
    private final AuthManager authManager;

    @Override
    public UserResponseDto execute(GetUserByEmailQuery query) {

        var user = authManager.validateUser(query.email());

        // ownership check
        authManager.checkOwnership(query.headerUserId() , user.getId());

        // exec common
        return commonGetUserByEmailService.execute(user);
    }
}
