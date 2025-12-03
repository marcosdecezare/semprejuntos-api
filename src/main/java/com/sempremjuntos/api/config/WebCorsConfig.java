package com.sempremjuntos.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global de CORS para recursos estáticos e endpoints específicos.
 * Aqui liberamos o acesso ao path /patients/** para qualquer origem,
 * permitindo que o FlutterFlow (domínio externo) consiga baixar as imagens.
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS para as imagens salvas em /patients/**
        registry.addMapping("/patients/**")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedHeaders("*");
    }
}
