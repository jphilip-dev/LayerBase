package com.jphilips.auth.service.user.query;

import com.jphilips.auth.dto.cqrs.query.GetUserByIdQuery;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.query.CommonGetUserByIdService;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.shared.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdService implements Query<GetUserByIdQuery, UserResponseDto> {

    private final CommonGetUserByIdService commonGetUserByIdService;
    private final AuthManager authManager;

    @Override
    public UserResponseDto execute(GetUserByIdQuery query) {
        // ownership check
        authManager.checkOwnership(query.headerUserId() ,query.userId());

        // exec common
        return commonGetUserByIdService.execute(query);
    }
}
