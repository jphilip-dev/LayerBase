package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.util.Command;
import com.jphilips.shared.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.dto.mapper.UserDetailsMapper;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonUpdateUserDetailsService implements Command<UpdateUserDetailsCommand, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final UserDetailsMapper userDetailsMapper;

    @Override
    public UserDetailsResponseDto execute(UpdateUserDetailsCommand command) {

        var dto = command.userDetailsRequestDto();

        var savedUserDetails = userDetailsManager.validateById(command.userDetailsId());

        savedUserDetails.setName(dto.getName());
        savedUserDetails.setAddress(dto.getAddress());
        savedUserDetails.setBirthDate(dto.getBirthDate());

        userDetailsManager.save(savedUserDetails);

        return userDetailsMapper.toDto(savedUserDetails);
    }
}
