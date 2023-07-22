package com.evampsanga.assignment.transformers;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.models.*;
import com.evampsanga.assignment.parsers.ResponseParser;
import com.evampsanga.assignment.responses.ResponseVO;
import com.evampsanga.assignment.utils.Utils;
import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
public class DataTransformer {


    private final List<DataTransformationStrategy> transformationStrategies;
    private final Utils utils;
    private final AppConfiguration config;
    private  ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public DataTransformer(List<DataTransformationStrategy> transformationStrategies, Utils utils, AppConfiguration config) {
        this.transformationStrategies = transformationStrategies;
        this.utils = utils;
        this.config = config;
    }

    public String transformToJSON(List<CsvData> csvDataList, DynamicConfiguration dynamicConfiguration) {
        List<Map<String, Object>> transformedDataList = new ArrayList<>();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("fname", getFileName(config.getInputFilePath()));
        responseMap.put("uuid", UUID.randomUUID().toString());
        for (CsvData csvData : csvDataList) {
            Map<String, Object> transformedData = transformCsvData(csvData, dynamicConfiguration);
            if (transformedData != null) {
                transformedDataList.add(transformedData);
            }
        }
        responseMap.put("payload", transformedDataList);
        ResponseParser responseParser = new ResponseParser();
        ResponseVO responseVO = responseParser.createResponseVOJson(responseMap);

        // Create the JSON string from the transformed data list
        String jsonOutput = "";
        try {
            jsonOutput = objectMapper.writeValueAsString(responseVO);
        } catch (JsonProcessingException e) {
            log.error("Error converting data to JSON: {}", e.getMessage());
        }
log.info("output json for API request:{}", jsonOutput.toString());
        return jsonOutput;
    }

    private Map<String, Object> transformCsvData(CsvData csvData, DynamicConfiguration dynamicConfiguration) {

        // Check if the data is valid based on the configuration rules
        if (!utils.validateData(csvData, dynamicConfiguration)) {
            log.warn("Invalid data found for CsvData with SystemId: {}", csvData.getSystemId());
            return null;
        }


        // Transform data based on the dynamic configuration and appropriate strategy
        Map<String, Object> transformedData = new HashMap<>();
        for (DynamicConfigurationField field : dynamicConfiguration.getFields()) {
            String fieldType = field.getFieldType().getTypeName();
            for (DataTransformationStrategy strategy : transformationStrategies) {
                if (strategy.supports(fieldType)) {
                    Map<String, Object> strategyTransformedData = strategy.transform(csvData, field, dynamicConfiguration);
                    if (strategyTransformedData != null ) {
                        if(!transformedData.keySet().containsAll(strategyTransformedData.keySet())){
                            transformedData.putAll(strategyTransformedData);
                        } else {
                            log.warn("redundant data :{}", strategyTransformedData.toString());
                        }

                    }
                    break;
                }
            } //end for strategy loop
        }
        log.info("transformCsvData: {}", transformedData.toString());
        return transformedData;
    }
    public static String getFileName(String path) {
        // Create a Path object from the input path
        Path filePath = Paths.get(path);

        // Get the filename from the Path object
        return filePath.getFileName().toString();
    }

}
