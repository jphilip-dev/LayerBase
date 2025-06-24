package com.jphilips.shared.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
    public PagedResponse(List<T> content, Page<?> page){
        this(content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }
}