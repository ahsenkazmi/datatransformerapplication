package com.evampsanga.assignment.transformers;
// ActionCodeTransformer.java
import com.evampsanga.assignment.interfaces.DataTransformationStrategy;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import com.evampsanga.assignment.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class ActionCodeTransformer implements DataTransformationStrategy {



    @Override
    public Map<String, Object> transform(CsvData csvData, DynamicConfigurationField field,  DynamicConfiguration dynamicConfiguration ) {
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

        // Map the action code to its corresponding value from the dynamic configuration
        String mappingKey = field.getMappingKey();
        if (mappingKey != null) {
            String transformedValue = dynamicConfiguration.tryMap(mappingKey, fieldValue);
            if (transformedValue != null) {
                jsonData.put(field.entityKey(), transformedValue);
            } else {
                log.warn("Action code not found in mapping for field: {} with value: {} for CsvData with SystemId: {}",
                        fieldName, fieldValue, csvData.getSystemId());
                return null;
            }
        }

        return jsonData;
    }

    @Override
    public boolean supports(String fieldType) {
        return  fieldType.equals("ActionCode");
    }

    // Other methods in ActionCodeTransformer (validateDataType, isValidInteger, isValidDecimal, isValidBoolean, isValidDate, formatDate) remain the same as in DataTransformer.
}
