package org.example.wellness_hub.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CaseInsensitiveEnumDeserializer<T extends Enum<T>> extends StdDeserializer<T> {

    private final Class<T> enumType;

    public CaseInsensitiveEnumDeserializer(Class<? extends Enum<?>> enumType) {
        super(enumType);
        this.enumType = (Class<T>) enumType;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        for (T constant : enumType.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(value)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Invalid value '" + value + "' for enum " + enumType.getSimpleName());
    }
}
