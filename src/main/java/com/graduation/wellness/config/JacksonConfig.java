package com.graduation.wellness.config;

import com.fasterxml.jackson.databind.Module;
import com.graduation.wellness.config.jackson.CaseInsensitiveEnumModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Module caseInsensitiveEnumModule() {
        return new CaseInsensitiveEnumModule();
    }
}
