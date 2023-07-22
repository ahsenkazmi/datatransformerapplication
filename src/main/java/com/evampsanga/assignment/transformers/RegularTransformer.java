package com.evampsanga.assignment.transformers;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.enums.DataType;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import com.evampsanga.assignment.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class
RegularTransformer implements DataTransformationStrategy {

    private final Utils utils;
    private final AppConfiguration config;
    private final TerminationHandler terminationHandler;
    public static final String REGULAR_STRATEGY = "Regular";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTOIN_CODE_DELETE = "delete";
    public static final String CONTRACT_END_DATE = "contract_endDate";

    @Autowired
    public RegularTransformer(Utils utils, AppConfiguration config, TerminationHandler terminationHandler) {
        this.utils = utils;
        this.config = config;
        this.terminationHandler = terminationHandler;
    }

    @Override
    public Map<String, Object> transform(CsvData csvData, DynamicConfigurationField field, DynamicConfiguration dynamicConfiguration) {
        Map<String, Object> jsonData = new HashMap<>();

        String fieldName = field.getSourceField();
        String fieldValue = csvData.getFieldValue(fieldName);

        if (fieldName.equalsIgnoreCase(CONTRACT_END_DATE)
                && csvData.getAction().equalsIgnoreCase(ACTOIN_CODE_DELETE)) {
            if (csvData.getContractWorkerId() == null || csvData.getContractWorkerId().isEmpty()) {
                return null; // Discard data if employee code is missing in case of termination
            }
            fieldValue = terminationHandler.handleTermination(csvData);
            csvData.setContractEndDate(fieldValue);
        }

        if (field.isMandatory() && (fieldValue == null || fieldValue.isEmpty())) {
            log.warn("Mandatory field missing: {} for CsvData with SystemId: {}", fieldName, csvData.getSystemId());
            return null;
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

        // Get the existing employee data for the same employee code (if provided)
        String employeeCode = csvData.getContractWorkerId();
        boolean isUpdate = csvData.getAction().equalsIgnoreCase(ACTION_UPDATE);
        if (isUpdate && (csvData.getContractWorkerId() != null && !csvData.getContractWorkerId().isEmpty())) {
            /*
             *  I haven't implemented the functionality for db insertion, reading/updation due to time constraint
             *  if the value for 'Action' is 'update' & we have employeeCode then we will update
             *  the data with new fields in db
             * */
            log.info("updating employee info block");
        }

        jsonData.put(field.entityKey(), fieldValue);

        log.info("fieldName :{} , fieldValue:{} , complete json:{}",fieldName, fieldValue, jsonData.toString());
        return jsonData;
    }

    @Override
    public boolean supports(String fieldType) {
        return fieldType.equals(REGULAR_STRATEGY);
    }

}
