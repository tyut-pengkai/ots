package org.dromara.common.translation.core.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class FieldToStringHandler extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node == null || node.isNull() || node.isEmpty()) {
            return null;
        }
        if (node.isTextual()) {
            String text = node.asText();
            if (isValidJson(text)) {
                return text;
            }
            return text;
        }
        return node.toString();
    }

    private boolean isValidJson(String text) {
        try {
            new ObjectMapper().readTree(text);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
