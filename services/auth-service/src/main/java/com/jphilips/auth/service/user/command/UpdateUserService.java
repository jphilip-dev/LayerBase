package com.jphilips.auth.service.user.command;

import com.jphilips.auth.service.AuthManager;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.service.common.command.CommonUpdateUserService;
import com.jphiilips.shared.domain.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final CommonUpdateUserService common;
    private final AuthManager authManager;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {

        // ownership check
        authManager.checkOwnership(command.headerUserId() ,command.userId());

        // exec common
        return  common.execute(command);

    }
}
