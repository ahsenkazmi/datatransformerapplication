package com.evampsanga.assignment.controllers;

import com.evampsanga.assignment.services.DataTransformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "data")
public class DataController {

    private final DataTransformationService dataTransformationService;

    public DataController(DataTransformationService dataTransformationService) {
        this.dataTransformationService = dataTransformationService;
    }

    @PostMapping("/")
    public ResponseEntity<String> processData(@RequestBody String data) {

        log.info("transformed data has been received" + data);
        return ResponseEntity.ok("Data received successfully");
    }

}
