package com.vteba.utils.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 反序列化器。
 * @author yinlei 
 * @see
 * @since 2013-12-1 12:11
 */
public class DateDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctx) throws IOException, JsonProcessingException {
        parser.getText();
        parser.getLongValue();
        // 然后做一些处理
        return null;
    }
}
