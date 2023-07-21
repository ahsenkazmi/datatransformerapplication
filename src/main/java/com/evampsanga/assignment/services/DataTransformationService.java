package com.evampsanga.assignment.services;

import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.parser.CsvParser;
import com.evampsanga.assignment.transformers.DataTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class DataTransformationService {

    private final CsvParser csvParser;
    private final DataTransformer dataTransformer;
    private final DynamicConfiguratoinService dynamicConfiguratoinService;

    @Autowired
    public DataTransformationService(CsvParser csvParser, DataTransformer dataTransformer, DynamicConfiguratoinService dynamicConfiguratoinService) {
        this.csvParser = csvParser;
        this.dataTransformer = dataTransformer;
        this.dynamicConfiguratoinService = dynamicConfiguratoinService;
    }

    public String transformData(String filePath, String configFilePath) {
        List<CsvData> csvDataList = csvParser.parseCsvFile(filePath);
        DynamicConfiguration dynamicConfiguration = dynamicConfiguratoinService.loadDynamicConfiguration(configFilePath);

        return dataTransformer.transformToJSON(csvDataList, dynamicConfiguration);
    }

}
