package com.jphilips.shared.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jphilips.shared.dto.ExceptionResponseDto;
import com.jphilips.shared.exceptions.custom.AppException;
import com.jphilips.shared.exceptions.custom.InternalCallException;
import com.jphilips.shared.exceptions.errorcode.CommonErrorCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public abstract class FeignCaller {

    protected <T> T callWithErrorHandling(
            Supplier<T> call,
            String sourceService,
            ObjectMapper objectMapper
    ) {
        try {
            return call.get();
        } catch (FeignException ex) {
            try {
                // Parse as tree
                JsonNode jsonNode = objectMapper.readTree(ex.contentUTF8());

                // Modify path field
                if (jsonNode instanceof ObjectNode objectNode) {
                    objectNode.put("path", "/internal-error/" + sourceService);
                }

                // Now map to your DTO
                var dto = objectMapper.treeToValue(jsonNode, ExceptionResponseDto.class);

                throw new InternalCallException(dto, sourceService);

            } catch (JsonProcessingException e) {

                log.warn("Feign call to {} failed with content: {}", sourceService, ex.contentUTF8());
                throw new AppException(CommonErrorCode.INTERNAL_CALL);
            }
        }
    }
}

