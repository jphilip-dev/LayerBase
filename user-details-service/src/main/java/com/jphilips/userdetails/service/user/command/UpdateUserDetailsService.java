package com.jphilips.userdetails.service.user.command;

import com.jphilips.shared.util.Command;
import com.jphilips.shared.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.service.UserDetailsManager;
import com.jphilips.userdetails.service.common.command.CommonUpdateUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserDetailsService implements Command<UpdateUserDetailsCommand, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final CommonUpdateUserDetailsService commonUpdateUserDetailsService;

    @Override
    public UserDetailsResponseDto execute(UpdateUserDetailsCommand command) {

        userDetailsManager.ownershipCheck(command.headerUserId(), command.userDetailsId());

        return commonUpdateUserDetailsService.execute(command);
    }
}
