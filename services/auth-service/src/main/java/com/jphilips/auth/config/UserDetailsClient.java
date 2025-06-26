package com.jphilips.auth.config;

import com.jphiilips.shared.domain.dto.UserDetailsRequestDto;
import com.jphiilips.shared.domain.dto.UserDetailsResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "configs/user-details-service",  url = "${USER_DETAILS_SERVICE_URL}")
public interface UserDetailsClient {

    @PostMapping("/internal/user-details")
    UserDetailsResponseDto createUserDetails(
            @Valid @RequestBody UserDetailsRequestDto userDetailsRequestDto
    );

    @PutMapping("/internal/user-details/{id}")
    UserDetailsResponseDto updateUserDetails(
            @PathVariable Long id,
            @Valid @RequestBody UserDetailsRequestDto userDetailsRequestDto
    );

    @DeleteMapping("/internal/user-details/{id}")
    void deleteUserDetails(@PathVariable Long id);

}
