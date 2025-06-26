package com.jphilips.userdetails.dto.cqrs;

import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import lombok.Builder;

@Builder
public record CreateUserDetailsCommand (
        UserDetailsRequestDto userDetailsRequestDto
) {
}
