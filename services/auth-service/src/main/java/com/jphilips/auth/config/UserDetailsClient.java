package com.jphilips.auth.config;

import com.jphilips.shared.dto.UserDetailsRequestDto;
import com.jphilips.shared.dto.UserDetailsResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-details-service",  url = "${USER_DETAILS_SERVICE_URL}")
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
