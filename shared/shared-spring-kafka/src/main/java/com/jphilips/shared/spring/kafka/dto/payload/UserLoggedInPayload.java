package com.jphilips.shared.spring.kafka.dto.payload;

import com.jphilips.shared.spring.kafka.dto.BasePayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoggedInPayload implements BasePayload {
    private Long userId;
}

