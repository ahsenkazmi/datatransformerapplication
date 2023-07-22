package com.evampsanga.assignment.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DataType {
    Text("Text"),
    Integer("Integer"),
    Decimal("Decimal"),
    Bool("Bool"),
    Date("Date");

    @Getter
    @JsonValue
    private final String typeName;
}
