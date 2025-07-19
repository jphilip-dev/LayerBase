package com.jphilips.userdetails.dto.cqrs;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record GetAllUserDetailsQuery (
        Pageable pageable
){
}
