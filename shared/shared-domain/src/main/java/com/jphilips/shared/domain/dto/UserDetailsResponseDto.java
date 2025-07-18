package com.jphilips.shared.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDetailsResponseDto{
        private Long id;
        private String name;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String address;
        private LocalDate birthDate;
}
