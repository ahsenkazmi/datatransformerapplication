package com.evampsanga.assignment.services;

import com.evampsanga.assignment.models.DynamicConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DynamicConfiguratoinService {


    private final ResourceLoader resourceLoader;

    @Autowired
    public DynamicConfiguratoinService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public DynamicConfiguration loadDynamicConfiguration(String configFilePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        // Get the resource from the resources folder
        Resource resource = resourceLoader.getResource("classpath:" + configFilePath);

        // Read the JSON content using an InputStream
        try  {
            InputStream inputStream = resource.getInputStream();
            DynamicConfiguration dynamicConfiguration = objectMapper.readValue(inputStream, DynamicConfiguration.class);
            return dynamicConfiguration;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
