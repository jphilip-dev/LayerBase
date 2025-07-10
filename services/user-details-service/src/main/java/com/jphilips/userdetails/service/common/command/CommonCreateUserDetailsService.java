package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.CreateUserDetailsCommand;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCreateUserDetailsService implements Command<CreateUserDetailsCommand, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetailsResponseDto execute(CreateUserDetailsCommand command) {

        var userDetails = userDetailsMapper.toEntity(command.userDetailsRequestDto());

        userDetailsManager.save(userDetails);

        return userDetailsMapper.toDto(userDetails);
    }
}
