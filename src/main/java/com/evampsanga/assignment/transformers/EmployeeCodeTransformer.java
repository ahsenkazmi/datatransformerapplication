package com.evampsanga.assignment.transformers;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.enums.DataType;
import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import com.evampsanga.assignment.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class EmployeeCodeTransformer implements DataTransformationStrategy {

    private final Utils utils;
    private final AppConfiguration config;
    private final TerminationHandler terminationHandler;
    public static final String EMPLOYEE_CODE_STRATEGY = "EmployeeCode";
    public static final String CONTRACT_WORKER_ID = "contract_workerId";
    public static final String ACTOIN_DELETE = "delete";
    public static final String ACTION_HIRE = "add";
    public static final String CONTRACT_END_DATE = "contract_endDate";
    public static final String EMPLOYEE_CODE_FORMAT = "yyMMdd";
    public static final int ORDER_NUMBER_LIMIT = 255;

    @Autowired
    public EmployeeCodeTransformer(Utils utils, AppConfiguration config, TerminationHandler terminationHandler) {
        this.utils = utils;
        this.config = config;
        this.terminationHandler = terminationHandler;
    }

    @Override
    public Map<String, Object> transform(CsvData csvData, DynamicConfigurationField field, DynamicConfiguration dynamicConfiguration) {
        Map<String, Object> jsonData = new HashMap<>();

        String fieldName = field.getSourceField();
        String fieldValue = csvData.getFieldValue(fieldName);

        // Generate employee code if not provided when hiring a new person
        if (fieldName.equalsIgnoreCase(CONTRACT_WORKER_ID)
                && csvData.getAction().equalsIgnoreCase(ACTION_HIRE)
                && (fieldValue == null || fieldValue.isEmpty())) {
            fieldValue = generateEmployeeCode(csvData);
            if (fieldValue == null) {
                jsonData.put("error", "employee code and first work day are missing for systemId:"+csvData.getSystemId());
                return jsonData; // Discard data if employee code and first work day are missing
            }
            csvData.setContractWorkerId(fieldValue);
        }

        if (fieldName.equalsIgnoreCase(CONTRACT_END_DATE)
                && csvData.getAction().equalsIgnoreCase(ACTOIN_DELETE)) {
            if (csvData.getContractWorkerId() == null || csvData.getContractWorkerId().isEmpty()) {
                jsonData.put("error", "employee code is missing for termination action for systemId:"+csvData.getSystemId());
                return jsonData; // Discard data if employee code is missing in case of termination
            }
            fieldValue = terminationHandler.handleTermination(csvData);
            csvData.setContractEndDate(fieldValue);
        }

        if (field.isMandatory() && (fieldValue == null || fieldValue.isEmpty())) {
            log.warn("Mandatory field missing: {} for CsvData with SystemId: {}", fieldName, csvData.getSystemId());
            jsonData.put("error", utils.getMandatoryErrorString(fieldName, csvData.getSystemId()));
            return jsonData;
        }

        // Validate data type if specified in the dynamic configuration
        if (field.getDataType() != null) {
            if (!Utils.validateDataType(fieldValue, field.getDataType(), field.getDateFormat())) {
                log.warn("Invalid data type for field: {} with value: {} for CsvData with SystemId: {}",
                        fieldName, fieldValue, csvData.getSystemId());
                return null;
            }
        }

        // Format date if specified in the dynamic configuration
        if (field.getDataType() == DataType.Date && field.getDateFormat() != null) {
            fieldValue = utils.formatDate(fieldValue, config.getGlobalDateFormat());
        }

        jsonData.put(field.entityKey(), fieldValue);
        log.info("fieldName :{} , fieldValue:{} , complete json:{}", fieldName, fieldValue, jsonData.toString());
        return jsonData;
    }

    @Override
    public boolean supports(String fieldType) {
        return fieldType.equals(EMPLOYEE_CODE_STRATEGY);
    }

    private String generateEmployeeCode(CsvData csvData) {
        String employeeCode = csvData.getContractWorkerId();
        if (employeeCode == null || employeeCode.isEmpty()) {
            String firstWorkDay = csvData.getContractWorkStartDate();
            if (firstWorkDay != null && !firstWorkDay.isEmpty()) {
                // Generate employee code based on first work day and an order number
                String orderNumber = getOrderNumber(ORDER_NUMBER_LIMIT);
                employeeCode = utils.formatDate(firstWorkDay, EMPLOYEE_CODE_FORMAT) + orderNumber;
            } else {
                log.warn("Employee code and first work day are missing for CsvData with SystemId: {}", csvData.getSystemId());
            }
        }
        return employeeCode;
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));

    }

    private static String getOrderNumber(int orderLimitNumber) {
        String orderNumber = String.format("%02X", new Random().nextInt(orderLimitNumber));
        log.info("generated hexa number:{}", orderNumber);
        return orderNumber;
    }
}
