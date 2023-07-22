package com.evampsanga.assignment.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FieldType {
    Regular("Regular"),
    ActionCode("ActionCode"),
    EmployeeCode("EmployeeCode");

    @Getter
    @JsonValue
    private final String typeName;
}
