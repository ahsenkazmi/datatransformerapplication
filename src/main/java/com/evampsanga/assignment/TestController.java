package com.evampsanga.assignment;

import com.evampsanga.assignment.services.DataTransformationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "test")
public class TestController {

    private final DataTransformationService dataTransformationService;

    public TestController(DataTransformationService dataTransformationService) {
        this.dataTransformationService = dataTransformationService;
    }

    @GetMapping("/")
    public void test(){
        dataTransformationService.transformData("files/input_01.csv", "files/dynamic_config.json");
    }

}
