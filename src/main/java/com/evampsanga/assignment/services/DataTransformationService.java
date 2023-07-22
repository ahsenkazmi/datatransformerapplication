package com.evampsanga.assignment.services;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.parsers.CsvParser;
import com.evampsanga.assignment.transformers.DataTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTransformationService {

    private final CsvParser csvParser;
    private final DataTransformer dataTransformer;
    private final DynamicConfiguratoinService dynamicConfiguratoinService;
    private final AppConfiguration appConfiguration;

    @Autowired
    public DataTransformationService(CsvParser csvParser, DataTransformer dataTransformer, DynamicConfiguratoinService dynamicConfiguratoinService, AppConfiguration appConfiguration) {
        this.csvParser = csvParser;
        this.dataTransformer = dataTransformer;
        this.dynamicConfiguratoinService = dynamicConfiguratoinService;
        this.appConfiguration = appConfiguration;
    }

    public String transformData(String filePath, String configFilePath) {
        List<CsvData> csvDataList = csvParser.parseCsvFile(appConfiguration.getInputFilePath());
        DynamicConfiguration dynamicConfiguration = dynamicConfiguratoinService.loadDynamicConfiguration(appConfiguration.getDynamicConfig());

        return dataTransformer.transformToJSON(csvDataList, dynamicConfiguration);
    }

}
