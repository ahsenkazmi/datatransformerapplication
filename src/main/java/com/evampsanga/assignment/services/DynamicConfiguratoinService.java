package com.evampsanga.assignment.services;

import com.evampsanga.assignment.models.DynamicConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class DynamicConfiguratoinService {
    private final ResourceLoader resourceLoader;

    @Autowired
    public DynamicConfiguratoinService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public DynamicConfiguration loadDynamicConfiguration(String configFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        DynamicConfiguration dynamicConfiguration = null;
        Resource resource = resourceLoader.getResource("classpath:" + configFilePath);
        try {
            InputStream inputStream = resource.getInputStream();
            dynamicConfiguration = objectMapper.readValue(inputStream, DynamicConfiguration.class);
        } catch (IOException e) {
            log.error("Exception occurred while loading dynamic_configuration file :{}", e.getMessage());
            e.printStackTrace();
        }
        return dynamicConfiguration;
    }
}
