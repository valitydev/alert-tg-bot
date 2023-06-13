package dev.vality.alert.tg.bot.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonMapper {

    private final ObjectMapper objectMapper;

    @SneakyThrows(JsonProcessingException.class)
    public String toJson(Object data) {
        return objectMapper.writeValueAsString(data);
    }

    @SneakyThrows(JsonProcessingException.class)
    public Map<String, String> toMap(String json) {
        return objectMapper.readValue(json, new TypeReference<>() {
        });
    }
}
