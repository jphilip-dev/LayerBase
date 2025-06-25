package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.FeignCallerHelper;
import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDeleteUserService implements Command<DeleteUserCommand, Void> {

    private final AuthManager authManager;
    private final FeignCallerHelper feignCallerHelper;

    private final UserDetailsClient userDetailsClient;

    @Override
    public Void execute(DeleteUserCommand command) {

        // Extract Payload
        var userId = command.userId();

        var user = authManager.validateUser(userId);

        // delete user details using feign S2S
        feignCallerHelper.execute(
                userDetailsClient.getClass().getSimpleName(),
                () -> {
                   userDetailsClient.deleteUserDetails(userId);
                   return null;
                });

        // Delete
        authManager.delete(user);

        return null;
    }
}
