package com.evampsanga.assignment.transformers;
import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DataType;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import com.evampsanga.assignment.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class EmployeeCodeTransformer implements DataTransformationStrategy {
    @Override
    public Map<String, Object> transform(CsvData csvData, DynamicConfigurationField field,  DynamicConfiguration dynamicConfiguration) {
        Map<String, Object> jsonData = new HashMap<>();

        String fieldName = field.getSourceField();
        String fieldValue = csvData.getFieldValue(fieldName);

        if (field.isMandatory() && (fieldValue == null || fieldValue.isEmpty())) {
            log.warn("Mandatory field missing: {} for CsvData with SystemId: {}", fieldName, csvData.getSystemId());
            return null;
        }

        // Validate data type if specified in the dynamic configuration
        if (field.getDataType() != null) {
            if (!Utils.validateDataType(fieldValue, field.getDataType())) {
                log.warn("Invalid data type for field: {} with value: {} for CsvData with SystemId: {}",
                        fieldName, fieldValue, csvData.getSystemId());
                return null;
            }
        }

        // Format date if specified in the dynamic configuration
        if (field.getDataType() == DataType.Date && field.getDateFormat() != null) {
            fieldValue = Utils.formatDate(fieldValue, field.getDateFormat());
        }

        jsonData.put(field.entityKey(), fieldValue);

        return jsonData;
    }

    @Override
    public boolean supports(String fieldType) {
        return fieldType.equals("EmployeeCode");
    }

    // Other methods in EmployeeCodeTransformer (validateDataType, formatDate) remain the same as in DataTransformer.
}
