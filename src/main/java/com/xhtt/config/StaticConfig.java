package com.xhtt.config;

import com.xhtt.common.utils.WordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaticConfig {
    @Value("${renren.file.path}")
    private String tempFilePath;

    @Bean
    public int initStatic() {
        WordUtils.tempFilePath = tempFilePath;
        return 0;
    }
}
