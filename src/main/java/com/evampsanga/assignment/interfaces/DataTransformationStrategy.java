package com.evampsanga.assignment.interfaces;

import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;

import java.util.Map;

public interface DataTransformationStrategy {
    Map<String, Object> transform(CsvData csvData, DynamicConfigurationField field, DynamicConfiguration dynamicConfiguration);
    boolean supports(String fieldType);
}
