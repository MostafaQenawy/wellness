package org.example.wellness_hub.config;

import com.fasterxml.jackson.databind.Module;
import org.example.wellness_hub.config.jackson.CaseInsensitiveEnumModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Module caseInsensitiveEnumModule() {
        return new CaseInsensitiveEnumModule();
    }
}
