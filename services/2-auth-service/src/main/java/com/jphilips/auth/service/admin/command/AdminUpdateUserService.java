package com.jphilips.auth.service.admin.command;

import com.jphilips.auth.config.RoleSeeder;
import com.jphilips.auth.dto.cqrs.command.UpdateUserCommand;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.auth.service.common.command.CommonUpdateUserService;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.redis.constant.CacheKeys;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUpdateUserService implements Command<UpdateUserCommand, UserResponseDto> {

    private final CommonUpdateUserService commonUpdateUserService;
    private final AuthManager authManager;
    private final RoleSeeder roleSeeder;

    private final RedisHelper redisHelper;

    @Override
    public UserResponseDto execute(UpdateUserCommand command) {

        var user = authManager.validateUser(command.userId());

        user.setIsActive(command.isActive());
        user.addRole(roleSeeder.getAdminRole());

        redisHelper.evictByTag(CacheKeys.Auth.AUTH_TOKEN_TAG + command.userId());

        // exec common
        return  commonUpdateUserService.execute(command, user);

    }
}
