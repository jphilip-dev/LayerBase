package com.jphilips.userdetails.service.common.command;

import com.jphilips.shared.util.Command;
import com.jphilips.userdetails.dto.cqrs.DeleteUserDetailsCommand;
import com.jphilips.userdetails.service.UserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDeleteUserDetailsService implements Command<DeleteUserDetailsCommand, Void> {

    private final UserDetailsManager userDetailsManager;

    @Override
    public Void execute(DeleteUserDetailsCommand command) {

        var userDetails = userDetailsManager.validateById(command.userDetailsId());

        userDetailsManager.delete(userDetails);

        return null;
    }
}
