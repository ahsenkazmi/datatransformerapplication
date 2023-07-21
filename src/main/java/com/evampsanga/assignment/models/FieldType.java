package com.evampsanga.assignment.models;


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
