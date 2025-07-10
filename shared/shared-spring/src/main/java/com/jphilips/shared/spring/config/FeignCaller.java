package com.jphilips.shared.spring.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jphilips.shared.domain.dto.ExceptionResponseDto;
import com.jphilips.shared.domain.exception.custom.AppException;
import com.jphilips.shared.domain.exception.custom.InternalCallException;
import com.jphilips.shared.domain.exception.errorcode.CommonErrorCode;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Slf4j
@RequiredArgsConstructor
public class FeignCaller {

    private final ObjectMapper objectMapper;

    public <T> T callWithErrorHandling(
            String sourceService,
            Supplier<T> call

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
                if(dto == null){
                    throw new NullPointerException();
                }
                throw new InternalCallException(dto, sourceService);

            } catch (NullPointerException | JsonProcessingException e) {

                log.warn("Feign call to {} failed with content: {}", sourceService, ex.contentUTF8());
                throw new AppException(CommonErrorCode.INTERNAL_CALL);
            }
        }
    }
}

