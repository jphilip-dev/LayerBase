package com.jphiilips.shared.domain.exception.util;

public record ApiError(
        String error,
        String message
) {
}
