package com.jphilips.auth.service.common.command;

import com.jphilips.auth.config.UserDetailsClient;
import com.jphilips.auth.dto.cqrs.command.DeleteUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphiilips.shared.domain.util.Command;
import com.jphilips.shared.spring.config.FeignCaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommonDeleteUserService implements Command<DeleteUserCommand, Void> {

    private final AuthManager authManager;
    private final FeignCaller feignCaller;

    private final UserDetailsClient userDetailsClient;

    @Override
    public Void execute(DeleteUserCommand command) {

        // Extract Payload
        var userId = command.userId();

        var user = authManager.validateUser(userId);

        // delete user details using feign S2S
        feignCaller.callWithErrorHandling(
                UserDetailsClient.class.getSimpleName(),
                () -> {
                   userDetailsClient.deleteUserDetails(userId);
                   return null;
                });

        // Delete
        authManager.delete(user);

        return null;
    }
}
