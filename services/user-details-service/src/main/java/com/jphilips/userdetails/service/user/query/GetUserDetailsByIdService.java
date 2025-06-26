package com.jphilips.userdetails.service.user.query;

import com.jphiilips.shared.domain.util.Query;
import com.jphiilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.GetUserDetailsByIdQuery;
import com.jphilips.userdetails.service.UserDetailsManager;
import com.jphilips.userdetails.service.common.query.CommonGetUserDetailsByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserDetailsByIdService implements Query<GetUserDetailsByIdQuery, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final CommonGetUserDetailsByIdService commonGetUserDetailsByIdService;

    @Override
    public UserDetailsResponseDto execute(GetUserDetailsByIdQuery query) {

        userDetailsManager.ownershipCheck(query.headerUserId(), query.userDetailsId());

        return commonGetUserDetailsByIdService.execute(query);
    }
}
