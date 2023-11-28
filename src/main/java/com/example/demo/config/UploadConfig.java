package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 3、路径映射也需要修改一下
         */
        registry.addResourceHandler(new String[] { "/file/**" }).addResourceLocations(new String[] { "file:/data/ftpServer/ftprepository/" });
    }
}






