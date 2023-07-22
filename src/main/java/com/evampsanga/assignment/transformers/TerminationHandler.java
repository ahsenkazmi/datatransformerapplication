package com.evampsanga.assignment.transformers;

import com.evampsanga.assignment.models.CsvData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TerminationHandler {

    public String handleTermination(CsvData csvData){
        String employeeCode = csvData.getContractWorkerId();
        String terminationDate = csvData.getContractEndDate();
        if (employeeCode != null || !employeeCode.isEmpty()) {
            if (terminationDate == null || terminationDate.isEmpty()) {
                //setting employee termination date to now
                terminationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
                csvData.setContractEndDate(terminationDate);
//                employeeCode = utils.formatDate(firstWorkDay, EMPLOYEE_CODE_FORMAT) + orderNumber;
            }
        }
        return terminationDate;
    }

}
