package com.jphilips.userdetails.service.common.query;

import com.jphilips.shared.dto.PagedResponse;
import com.jphilips.shared.util.Query;
import com.jphilips.shared.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.GetAllUserDetailsQuery;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.entity.UserDetails;
import com.jphilips.userdetails.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonGetAllUserDetailsService implements Query<GetAllUserDetailsQuery, PagedResponse<UserDetailsResponseDto>> {

    private final UserDetailsRepository userDetailsRepository;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public PagedResponse<UserDetailsResponseDto> execute(GetAllUserDetailsQuery query) {

        Page<UserDetails> userDetailsPage = userDetailsRepository.findAll(query.pageable());

        List<UserDetailsResponseDto> content = userDetailsPage.getContent().stream()
                .map(userDetailsMapper::toDto)
                .toList();

        return new PagedResponse<>(content,userDetailsPage);
    }
}
