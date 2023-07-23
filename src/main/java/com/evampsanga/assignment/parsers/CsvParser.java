package com.evampsanga.assignment.parsers;

import com.evampsanga.assignment.models.CsvData;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CsvParser {

    private final ResourceLoader resourceLoader;

    @Autowired
    public CsvParser(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<CsvData> parseCsvFile(String filePath) {
        List<CsvData> csvDataList = new ArrayList<>();
        boolean isFirstRow = true;
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            File file = resource.getFile();
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip the first row (header row)
                }
                CsvData rowData = createCsvData(nextLine);
                csvDataList.add(rowData);
            }
        } catch (IOException e) {
            log.error("IO Exception occurred while reading csv:{}", e.getMessage());
            e.printStackTrace();
        } catch (CsvValidationException e) {
            log.error("csv file validation failed:{}", e.getMessage());
            e.printStackTrace();
        }
        return csvDataList;
    }

    private CsvData createCsvData(String[] row) {

        CsvData csvData = new CsvData();
        csvData.setSystemId(row[0]);
        csvData.setAction(row[1]);
        csvData.setWorkerName(row[2]);
        csvData.setWorkerPersonalCode(row[3]);
        csvData.setWorkerGender(row[4]);
        csvData.setWorkerNumberOfKids(row[5]);
        csvData.setWorkerMotherMaidenName(row[6]);
        csvData.setWorkerGrandmotherMaidenName(row[7]);
        csvData.setContractSignatureDate(row[8]);
        csvData.setContractWorkStartDate(row[9]);
        csvData.setContractType(row[10]);
        csvData.setContractEndDate(row[11]);
        csvData.setContractWorkerId(row[12]);
        csvData.setPayAmount(row[13]);
        csvData.setPayCurrency(row[14]);
        csvData.setPayEffectiveFrom(row[15]);
        csvData.setPayEffectiveTo(row[16]);
        csvData.setCompensationAmount(row[17]);
        csvData.setCompensationType(row[18]);
        csvData.setCompensationCurrency(row[19]);
        csvData.setCompensationEffectiveFrom(row[20]);
        csvData.setCompensationEffectiveTo(row[21]);
        log.info("csvData obj {}", csvData.toString());
        return csvData;
    }
}
