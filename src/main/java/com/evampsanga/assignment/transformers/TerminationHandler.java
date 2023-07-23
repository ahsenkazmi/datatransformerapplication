package com.evampsanga.assignment.transformers;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.models.CsvData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TerminationHandler {
    private final AppConfiguration configuration;

    public TerminationHandler(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    public String handleTermination(CsvData csvData) {
        String employeeCode = csvData.getContractWorkerId();
        String terminationDate = csvData.getContractEndDate();
        if (employeeCode != null || !employeeCode.isEmpty()) {
            if (terminationDate == null || terminationDate.isEmpty()) {
                //setting employee termination date to now
                terminationDate = LocalDate.now().format(DateTimeFormatter.ofPattern(configuration.getGlobalDateFormat()));
                csvData.setContractEndDate(terminationDate);
            }
        }
        return terminationDate;
    }

}
