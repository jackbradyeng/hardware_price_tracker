package com.price_tracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* Cross-Origin Resource Sharing (CORS) permissions */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    // TypeScript-React-Vite front-end hosted on port 3000
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v*/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
    }
}