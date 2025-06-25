package com.jphilips.userdetails.dto.cqrs;

import com.jphilips.shared.dto.UserDetailsRequestDto;
import lombok.Builder;

@Builder
public record UpdateUserDetailsCommand(
        Long headerUserId,
        Long userDetailsId,
        UserDetailsRequestDto userDetailsRequestDto
) {
}
