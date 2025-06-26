package com.jphilips.userdetails.dto.mapper;

import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphiilips.shared.domain.dto.UserDetailsResponseDto;
import com.jphilips.userdetails.entity.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {

    public UserDetailsResponseDto toDto(UserDetails userDetails){
        return UserDetailsResponseDto.builder()
                .id(userDetails.getId())
                .name(userDetails.getName())
                .address(userDetails.getAddress())
                .birthDate(userDetails.getBirthDate())
                .build();
    }

    public UserDetails toEntity(UserDetailsRequestDto dto){
        return UserDetails.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .build();
    }

}
