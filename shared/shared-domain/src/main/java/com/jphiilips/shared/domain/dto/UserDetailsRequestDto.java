package com.jphiilips.shared.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetailsRequestDto {

    @NotNull(message = "{required.notnull}")
    private Long id;

    @NotNull(message = "{required.notnull}")
    private String name;

    private String address;

    @NotNull(message = "{required.notnull}")
    private LocalDate birthDate;
}
