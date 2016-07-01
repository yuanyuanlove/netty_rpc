package com.rpc.netty.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanyuan on 2016/5/29.
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static <T> T parseObject(String jsonStr, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonStr, clazz);
    }

    public static <T> List<T> parseArrayList(String jsonStr, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, new Class[]{clazz});
        return (List)objectMapper.readValue(jsonStr, javaType);
    }

    public static <T> T parseObjectUncheck(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            throw new RuntimeException("jackson format error: " + obj.getClass(), var2);
        }
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
