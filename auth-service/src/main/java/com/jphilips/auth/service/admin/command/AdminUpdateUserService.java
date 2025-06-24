package com.jphilips.auth.service.admin.command;

import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.command.CommonUpdateUserService;
import com.jphilips.shared.dto.UserResponseDto;
import com.jphilips.shared.util.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final CommonUpdateUserService commonUpdateUserService;
    private final AuthManager authManager;
    private final RoleSeeder roleSeeder;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {

        var user = authManager.validateUser(command.userId());

        user.setIsActive(command.isActive());
        user.addRole(roleSeeder.getAdminRole());

        // exec common
        return  commonUpdateUserService.execute(command, user);

    }
}
