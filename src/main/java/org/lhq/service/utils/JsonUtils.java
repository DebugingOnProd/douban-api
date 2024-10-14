package org.lhq.service.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class JsonUtils {

    private static final String  pattern = "yyyy-MM-dd HH:mm:ss";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    static {
        log.info("JsonUtils config init");
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<>() {

            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ofPattern(pattern)));
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<>() {
            @Override
            public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(pattern));
            }
        });
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(javaTimeModule);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("JsonUtils config init success");
    }

    private JsonUtils(){
    }

    public static String toJson(Object obj){
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json序列化出错：",e);
        }
        return null;
    }

    public static <T> T fromJson(String json,Class<T> tClass){
        try {
            return mapper.readValue(json,tClass);
        } catch (Exception e) {
            log.error("json解析出错：",e);
        }
        return null;
    }

    public static <T> T fromFileReader(FileReader fileReader, Class<T> tClass){
        try {
            return mapper.readValue(fileReader, tClass);
        } catch (IOException e) {
            log.error("读取json文件时发生错误：",e);
            return null;
        }
    }


    public static <T> List<T> readJsonToList(String json, Class<T> tClass){
        try {
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            return mapper.readValue(json, typeFactory.constructCollectionType(List.class, tClass));
        }catch (IOException e) {
            log.error("读取json文件时发生错误：",e);
            return Collections.emptyList();
        }
    }



    public static <T> List<T> readJsonToList(FileReader fileReader, Class<T> tClass){
        try {
            TypeFactory typeFactory = TypeFactory.defaultInstance();
            return mapper.readValue(fileReader, typeFactory.constructCollectionType(List.class, tClass));
        }catch (IOException e) {
            log.error("读取json文件时发生错误：",e);
            return Collections.emptyList();
        }
    }


}
