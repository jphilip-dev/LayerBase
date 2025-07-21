package com.jphilips.shared.spring.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jphilips.shared.spring.kafka.dto.AppEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Slf4j
@Service
@RequiredArgsConstructor
public class DLTHandler {

    private final ObjectMapper objectMapper;

    public String handle(byte[] data, String originalTopic, String error) {

        String raw = new String(data, StandardCharsets.UTF_8);
        log.warn("Received DLT message from [{}],  Error: [{}]", originalTopic, error);

        try {
            AppEvent<?> parsed = objectMapper.readValue(data, AppEvent.class);
            log.info("Parsed AppEvent from DLT Directly: {}", parsed);
            return parsed.toString();
        } catch (Exception e1) {
            log.error("Couldn't parse AppEvent directly");
            try {
                // Remove double quotes
                if (raw.startsWith("\"") && raw.endsWith("\"")) {
                    raw = raw.substring(1, raw.length() - 1);
                }
                // Decode from Base64 to get actual JSON
                byte[] decoded = Base64.getDecoder().decode(raw);

                String decodedJson = new String(decoded, StandardCharsets.UTF_8);

                log.info("Decoded raw json: \n{}", decodedJson);
                return decodedJson;
            } catch (Exception e2) {
                log.error("Couldn't parse raw json : {}", e2.getMessage());
            }

        }

        return null;
    }
}

