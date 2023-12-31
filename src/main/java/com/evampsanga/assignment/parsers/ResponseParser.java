package com.evampsanga.assignment.parsers;

import com.evampsanga.assignment.responses.PayComponent;
import com.evampsanga.assignment.responses.Payload;
import com.evampsanga.assignment.responses.ResponseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseParser {

    public ResponseVO createResponseVOJson(Map<String, Object> responseMap) {
        List<ResponseVO> responseVoList = new ArrayList<>();
        ResponseVO responseVo = new ResponseVO();
        List<String> errors = new ArrayList<>();
        responseVo.setUuid(getStringValue(responseMap.get("uuid")));
        responseVo.setFname(getStringValue(responseMap.get("fname")));
        List<Payload> payloads = new ArrayList<>();
        for (Map<String, Object> transformedData : (List<Map<String, Object>>) responseMap.get("payload")) {
            if(transformedData.get("errors") != null){
                errors.addAll(getErrorsValue((List<String>) transformedData.get("errors")));
                continue;
            }
            Payload payload = createPayloadJson(transformedData);
            payloads.add(payload);
        }
        responseVo.setPayload(payloads);
        responseVo.setErrors(errors);
        return responseVo;
    }

    public Payload createPayloadJson(Map<String, Object> transformedData) {
        Payload payload = new Payload();
        payload.setEmployeeCode(getStringValue(transformedData.get("contract_workerid")));
        payload.setAction(getStringValue(transformedData.get("action")));
        Map<String, String> data = new HashMap<>();
        data.put("person.full_name", getStringValue(transformedData.get("person.full_name")));
        data.put("person.gender", getStringValue(transformedData.get("person.gender")));
        data.put("person.birthdate", getStringValue(transformedData.get("person.birthdate")));
        data.put("person.hire_date", getStringValue(transformedData.get("person.hire_date")));
        data.put("person.termination_date", getStringValue(transformedData.get("person.termination_date")));
        payload.setData(data);
        payload.setPayComponents(List.of(createPaymentComponentJson(transformedData)));
        return payload;
    }

    private PayComponent createPaymentComponentJson(Map<String, Object> transformedData) {
        PayComponent payComponent = new PayComponent();
        payComponent.setAmount(getDoubleValue(transformedData.get("salary_component.amount")));
        payComponent.setCurrency(getStringValue(transformedData.get("salary_component.currency")));
        payComponent.setStartDate(getStringValue(transformedData.get("salary_component.start_date")));
        payComponent.setEndDate(getStringValue(transformedData.get("salary_component.end_date")));
        return payComponent;
    }

    private List<String> getErrorsValue(List<String> errors) {
        if (errors != null  && errors.size() > 0) {
            return errors;
        } else {
            return new ArrayList<>(); // or return an empty list, depending on your use case
        }
    }
    private String getStringValue(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    private Double getDoubleValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Double) {
            return (Double) value;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
