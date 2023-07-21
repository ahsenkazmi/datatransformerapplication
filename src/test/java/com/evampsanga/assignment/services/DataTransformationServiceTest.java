package com.evampsanga.assignment.services;

import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.parser.CsvParser;
//import com.evampsanga.assignment.transformers.Transformer;
//import com.evampsanga.assignment.config.DynamicConfiguration;
//import com.evampsanga.assignment.entities.CsvData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DataTransformationServiceTest {

    @Mock
    private CsvParser csvParser;

    @Mock
    private DataTransformationStrategy dataTransformationStrategy;

    @InjectMocks
    private DataTransformationService dataTransformationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testTransformData() {
//        // Arrange
//        String filePath = "input_01.csv";
//        String configFilePath = "dynamic_config.json";
//        String jsonData = "{\"result\": \"transformed data\"}";
//
//        CsvData csvData = new CsvData(); // Set up your test data here
//
////        when(csvParser.parseCsvFile(filePath)).thenReturn(Collections.singletonList(csvData));
////        when(dataTransformationStrategy.transform(eq(csvData), any(DynamicConfiguration.class))).thenReturn(jsonData);
//
//        // Act
//        String result = dataTransformationService.transformData(filePath, configFilePath);
//
//        // Assert
//        assertEquals(jsonData, result);
//
//        // Verify that the parseCsvFile and transform methods were called with the correct arguments
//        verify(csvParser).parseCsvFile(filePath);
//        verify(dataTransformationStrategy).transform(eq(csvData), any(DynamicConfiguration.class));
//    }
}
