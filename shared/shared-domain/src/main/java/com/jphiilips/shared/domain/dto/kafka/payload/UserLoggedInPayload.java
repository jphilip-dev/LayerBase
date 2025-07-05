package com.jphiilips.shared.domain.dto.kafka.payload;

import com.jphiilips.shared.domain.dto.kafka.BasePayload;
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

