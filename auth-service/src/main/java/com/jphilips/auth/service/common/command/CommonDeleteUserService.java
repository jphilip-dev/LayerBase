package com.jphilips.auth.service.common.command;

import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDeleteUserService implements Command<DeleteUserCommand, Void> {

    private final AuthManager authManager;

    @Override
    public Void execute(DeleteUserCommand command) {

        // Extract Payload
        var userId = command.userId();

        var user = authManager.validateUser(userId);

        authManager.delete(user);

        return null;
    }
}
