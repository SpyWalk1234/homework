package com.wandoo.homework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Objects.requireNonNull;

public class JsonConverter {
    public static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModules(new JavaTimeModule());
        mapper.disable(WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(FAIL_ON_EMPTY_BEANS);
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @SneakyThrows(IOException.class)
    public static String toJsonString(Object source) {
        requireNonNull(source);
        return mapper.writeValueAsString(source);
    }

    @SneakyThrows(IOException.class)
    public static JsonNode getTree(String from) {
        return mapper.readTree(from);
    } //ignore field order during comparison

}
