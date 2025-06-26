package com.jphilips.auth.dto.mapper;

import com.jphilips.auth.dto.UserRequestDto;
import com.jphiilips.shared.domain.dto.UserResponseDto;
import com.jphilips.auth.entity.Role;
import com.jphilips.auth.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }

    public UserResponseDto toDto(User user, String requestId){
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .requestId(requestId)
                .build();
    }

    public User toEntity(UserRequestDto dto){
        return User.builder()
                .email(dto.getEmail().toLowerCase())
                .password(dto.getPassword())
                .build();
    }

}
