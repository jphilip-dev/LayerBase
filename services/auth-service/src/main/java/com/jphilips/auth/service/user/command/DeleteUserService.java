package com.jphilips.auth.service.user.command;

import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.command.CommonDeleteUserService;
import com.jphiilips.shared.domain.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserService implements Command<DeleteUserCommand, Void> {

    private final AuthManager authManager;
    private final CommonDeleteUserService common;

    @Override
    public Void execute(DeleteUserCommand command) {

        // ownership check
        authManager.checkOwnership(command.headerUserId() ,command.userId());

        common.execute(command);

        return null;
    }
}
