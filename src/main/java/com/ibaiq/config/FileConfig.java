package com.ibaiq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 映射文件地址配置类
 *
 * @author 十三
 */
@Configuration
@ConfigurationProperties(prefix = "ibaiq.file")
public class FileConfig implements WebMvcConfigurer {

    @Autowired
    private IbaiqConfig ibaiq;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + ibaiq.getProfile());
    }

}
