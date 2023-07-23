package com.evampsanga.assignment.models;

import com.evampsanga.assignment.enums.DataType;
import com.evampsanga.assignment.enums.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class DynamicConfigurationField {

    private FieldType fieldType;

    @JsonFormat(shape = JsonFormat.Shape.BOOLEAN)
    private boolean isMandatory = false;

    private String sourceField;
    private String targetEntity;
    private String targetField;

    private DataType dataType;

    private String mappingKey;

    private String dateFormat;

    private String validationPattern;

    private Integer regexCaptureGroupNr;

    public String entityKey() {
        return targetEntity == null ? this.getSourceField().toLowerCase() : targetEntity + "." + targetField;
    }
}
