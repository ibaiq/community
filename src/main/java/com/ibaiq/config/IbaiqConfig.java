package com.ibaiq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

/**
 * @author 十三
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ibaiq")
public class IbaiqConfig {

    private String profile;

    private String tokenHeader;

    private String prefix;

    private String secret;

    private Duration expired;

    private String avatar;

    private String images;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                  .allowedOrigins("*")
                                  .allowedMethods("*")
                                  .allowedHeaders("*")
                                  .maxAge(3600);
            }
        };
    }

}
