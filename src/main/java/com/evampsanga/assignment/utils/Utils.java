package com.evampsanga.assignment.utils;

import com.evampsanga.assignment.configs.AppConfiguration;
import com.evampsanga.assignment.models.CsvData;
import com.evampsanga.assignment.models.DataType;
import com.evampsanga.assignment.models.DynamicConfiguration;
import com.evampsanga.assignment.models.DynamicConfigurationField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public boolean validateData(CsvData csvData, DynamicConfiguration dynamicConfiguration) {
        for (DynamicConfigurationField field : dynamicConfiguration.getFields()) {
            if (field.isMandatory()) {
                String fieldName = field.getSourceField();
                if (!csvData.hasField(fieldName)) {
                    log.warn("Mandatory field missing: {} for CsvData with SystemId: {}", fieldName, csvData.getSystemId());
                    return false;
                }
            }
            // Validate data type if specified in the dynamic configuration
            String fieldValue = csvData.getFieldValue(field.getSourceField());
            if(field.getValidationPattern() != null){
                  fieldValue = validateWithRegex(fieldValue, field.getValidationPattern(), field.getRegexCaptureGroupNr() == null ? 0 :  field.getRegexCaptureGroupNr() );
            }

            if (!field.isMandatory() && fieldValue.isEmpty()) {
                continue;
            }
            if (!(validateDataType(fieldValue, field.getDataType()) )) {
                return false;
            }
        }
        return true;
    }

    // DataTransformer class
    public static boolean validateDataType(String value, DataType dataType) {
        switch (dataType) {
            case Text:
                return true; // No specific validation for Text type, return true for simplicity
            case Integer:
                return isValidInteger(value);
            case Decimal:
                return isValidDecimal(value);
            case Bool:
                return isValidBoolean(value);
            case Date:
                return isValidDate(value, DynamicConfiguration.GLOBAL_DATE_FORMAT);
            default:
                return true; // Handle other data types as per requirements
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
            return false;
        }
    }

    // Implement the date formatting logic
    public  String formatDate(String value, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = sdf.parse(value);
            return sdf.format(date);
        } catch (ParseException e) {
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
                    log.debug("group number  not mentioned:" );
                    return input;
                }
            } catch (IndexOutOfBoundsException e) {
                // If the specified group number is invalid, return null
                System.out.println("Invalid group number: " + groupNumber);
                return null;
            }
        } else {
            // Input string does not match the regex
            return null;
        }
    }

}
