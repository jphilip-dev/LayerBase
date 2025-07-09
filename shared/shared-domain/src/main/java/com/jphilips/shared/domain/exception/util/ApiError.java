package com.jphilips.shared.domain.exception.util;

public record ApiError(
        String error,
        String message
) {
}
