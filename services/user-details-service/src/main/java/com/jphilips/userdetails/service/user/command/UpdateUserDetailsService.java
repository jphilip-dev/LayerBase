package com.jphilips.userdetails.service.user.command;

import com.jphiilips.shared.domain.util.Command;
import com.jphiilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.dto.cqrs.UpdateUserDetailsCommand;
import com.jphilips.userdetails.service.UserDetailsManager;
import com.jphilips.userdetails.service.common.command.CommonUpdateUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserDetailsService implements Command<UpdateUserDetailsCommand, UserDetailsResponseDto> {

    private final UserDetailsManager userDetailsManager;
    private final CommonUpdateUserDetailsService commonUpdateUserDetailsService;

    @Override
    public UserDetailsResponseDto execute(UpdateUserDetailsCommand command) {

        userDetailsManager.ownershipCheck(command.headerUserId(), command.userDetailsId());

        // logging
        log.info("User: {} Updated details", command.headerUserId());

        return commonUpdateUserDetailsService.execute(command);
    }
}
