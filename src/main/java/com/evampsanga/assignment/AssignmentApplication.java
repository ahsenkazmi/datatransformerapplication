package com.evampsanga.assignment;

import com.evampsanga.assignment.services.DataTransformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
public class AssignmentApplication {

    @Autowired
    DataTransformationService dataTransformationService;
    @Autowired
    RestTemplate restTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AssignmentApplication.class, args);
        AssignmentApplication app = context.getBean(AssignmentApplication.class);
        app.runDataTransformation();
    }

    public void runDataTransformation() {
        // Perform data transformations
        String transformedData = dataTransformationService.transformData();

        // Send the transformed data to the API endpoint
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/data/", transformedData, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Data successfully sent to API.");
        } else {
            log.warn("Failed to send data to API. Status code: " + response.getStatusCodeValue());
        }
    }
}
