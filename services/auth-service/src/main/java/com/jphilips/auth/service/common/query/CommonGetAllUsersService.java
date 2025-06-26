package com.jphilips.auth.service.common.query;

import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.query.GetAllUsersQuery;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.entity.User;
import com.jphilips.auth.repository.UserRepository;
import com.jphilips.shared.spring.util.PagedResponse;
import com.jphiilips.shared.domain.util.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonGetAllUsersService implements Query<GetAllUsersQuery, PagedResponse<UserResponseDto>> {

    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    @Override
    public PagedResponse<UserResponseDto> execute(GetAllUsersQuery query) {

        Page<User> userPage = userRepository.findAll(query.pageable());

        List<UserResponseDto> content = userPage.getContent().stream()
                .map(authMapper::toDto)
                .toList();

        return new PagedResponse<>(content, userPage);
    }
}
