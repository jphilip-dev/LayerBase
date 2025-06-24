package com.jphilips.auth.dto.mapper;

import com.jphilips.auth.dto.RegisterRequestDto;
import com.jphilips.auth.entity.Role;
import com.jphilips.auth.entity.User;
import com.jphilips.shared.dto.AuthResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthResponseDto toDto(User user){
        return AuthResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }

    public User toEntity(RegisterRequestDto dto){
        return User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

}
