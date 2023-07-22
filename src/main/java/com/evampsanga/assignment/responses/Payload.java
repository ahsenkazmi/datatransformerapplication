package com.evampsanga.assignment.responses;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Payload {

    private String employeeCode;
    private String action;
    private Map<String, String> data;
    private List<PayComponent> payComponents;

}
