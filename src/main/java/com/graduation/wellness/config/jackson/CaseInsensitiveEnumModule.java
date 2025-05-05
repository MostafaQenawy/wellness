package com.graduation.wellness.config.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CaseInsensitiveEnumModule extends SimpleModule {
    public CaseInsensitiveEnumModule() {
        setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config,
                                                              JavaType type,
                                                              BeanDescription beanDesc,
                                                              JsonDeserializer<?> deserializer) {
                Class<?> rawClass = type.getRawClass();
                if (Enum.class.isAssignableFrom(rawClass)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) rawClass;
                    return new CaseInsensitiveEnumDeserializer<>(enumClass);
                }
                return deserializer;
            }
        });
    }
}
