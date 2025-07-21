package com.jphilips.auth.service.auth.command;

import com.jphilips.auth.dto.cqrs.command.ActivationCommand;
import com.jphilips.auth.dto.mapper.AuthMapper;
import com.jphilips.auth.exceptions.custom.OtpMismatchException;
import com.jphilips.auth.service.AuthManager;
import com.jphilips.shared.domain.dto.UserResponseDto;
import com.jphilips.shared.domain.exception.errorcode.AuthErrorCode;
import com.jphilips.shared.domain.util.Command;
import com.jphilips.shared.spring.redis.constant.CacheKeys;
import com.jphilips.shared.spring.redis.service.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationService  implements Command<ActivationCommand, UserResponseDto> {

    private final RedisHelper redisHelper;
    private final AuthManager authManager;

    private final AuthMapper authMapper;

    @Override
    public UserResponseDto execute(ActivationCommand command) {

        var user = authManager.validateUser(command.email());

        String cacheKey = CacheKeys.Auth.AUTH_OTP_BY_USER_ID + user.getId();

        var cachedOtp = redisHelper.get(cacheKey, String.class);

        if (cachedOtp != null && cachedOtp.equalsIgnoreCase(command.otp())){
            authManager.activateUserStatus(user);
            redisHelper.delete(cacheKey);
        } else {
            throw new OtpMismatchException(AuthErrorCode.OTP_MISMATCH);
        }

        return authMapper.toDto(user);
    }
}
