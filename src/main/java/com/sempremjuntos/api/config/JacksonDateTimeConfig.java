package com.sempremjuntos.api.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Formatação global de datas no JSON no formato "dd-MM-yyyy HH:mm".
 * Funciona universalmente para todos os LocalDateTime do projeto.
 */
@Configuration
public class JacksonDateTimeConfig {

    private static final String PATTERN = "dd/MM/yyyy HH:mm";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

            JavaTimeModule module = new JavaTimeModule();
            module.addSerializer(java.time.LocalDateTime.class, new LocalDateTimeSerializer(formatter));

            builder.modules(module);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo")));
        };
    }
}
