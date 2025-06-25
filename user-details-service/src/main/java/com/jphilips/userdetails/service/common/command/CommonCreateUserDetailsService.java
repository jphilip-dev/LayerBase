package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.util.Command;
import com.jphilips.shared.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.CreateUserDetailsCommand;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
