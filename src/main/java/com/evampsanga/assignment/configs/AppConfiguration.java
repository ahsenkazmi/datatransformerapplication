package com.evampsanga.assignment.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "system.config")
public class AppConfiguration {
    private String inputFilePath;
    private String dynamicConfig;
    private String globalDateFormat;
}


