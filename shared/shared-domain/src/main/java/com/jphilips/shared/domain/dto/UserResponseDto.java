package com.jphilips.shared.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDto{
        private Long id;
        private String email;
        private Boolean isActive;
        private List<String> roles;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String requestId;
}