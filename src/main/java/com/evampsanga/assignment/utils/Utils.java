package com.evampsanga.assignment.utils;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.enums.DataType;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class Utils {

    private final AppConfiguration configs;

    @Autowired
    public Utils(AppConfiguration configs) {
        this.configs = configs;
    }

    public List<String> validateData(CsvData csvData, DynamicConfiguration dynamicConfiguration) {
        List<String> errors = new ArrayList<>();
        for (DynamicConfigurationField field : dynamicConfiguration.getFields()) {
            if (field.isMandatory()) {
                String fieldName = field.getSourceField();
                if (!csvData.hasField(fieldName)) {
                    log.warn("Mandatory field missing: {} for CsvData with SystemId: {}", fieldName, csvData.getSystemId());
                    errors.add(getMandatoryErrorString( fieldName, csvData.getSystemId()));
                }
            }
            // Validate data type if specified in the dynamic configuration
            String fieldValue = csvData.getFieldValue(field.getSourceField());
            if (field.getValidationPattern() != null) {
                fieldValue = validateWithRegex(fieldValue, field.getValidationPattern(), field.getRegexCaptureGroupNr() == null ? 0 : field.getRegexCaptureGroupNr());
            }
            if (!field.isMandatory() && fieldValue.isEmpty()) {
                continue;
            }
            if (!(validateDataType(fieldValue, field.getDataType(), field.getDateFormat()))) {
                errors.add("Invalid data found for "+field.getSourceField()+", for data with systemId"+ csvData.getSystemId());

            }
        }
        return errors;
    }

    public static boolean validateDataType(String value, DataType dataType, String dateFormat) {
        switch (dataType) {
            case Text:
                return true;
            case Integer:
                return isValidInteger(value);
            case Decimal:
                return isValidDecimal(value);
            case Bool:
                return isValidBoolean(value);
            case Date:
                return isValidDate(value, dateFormat);
            default:
                return true;
        }
    }

    private static boolean isValidInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidDecimal(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    private static boolean isValidDate(String value, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            log.warn("date validation failed, returning false");
            return false;
        }
    }

    // Implement the date formatting logic
    public String formatDate(String value, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(value);
            return sdf.format(date);
        } catch (ParseException e) {
            log.warn("date formatting failed, returning original value");
            return value; // Return the original value if date parsing fails
        }
    }

    private static String validateWithRegex(String input, String regex, int groupNumber) {
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // Check if the input string matches the regex
        if (matcher.matches()) {
            try {
                if (groupNumber > 0 && groupNumber <= matcher.groupCount()) {
                    // Try to get the capture group specified by the groupNumber
                    return matcher.group(groupNumber);
                } else {
                    log.debug("group number not mentioned");
                    return input;
                }
            } catch (IndexOutOfBoundsException e) {
                // If the specified group number is invalid, return null
                log.error("Invalid group number:{}, return null", groupNumber);
                return null;
            }
        } else {
            log.info("Input string does not match the regex");
            return null;
        }
    }

    public String getMandatoryErrorString(String fieldName, String systemId){
        return "Mandatory field: "+fieldName+" missing from CsvData with SystemId:"+  systemId;
    }

}
