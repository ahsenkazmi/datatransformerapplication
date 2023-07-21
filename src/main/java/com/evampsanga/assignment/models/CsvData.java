package com.evampsanga.assignment.models;

import lombok.Data;
import org.springframework.stereotype.Component;


@Data
public class CsvData {
    private String systemId;
    private String action;
    private String workerName;
    private String workerPersonalCode;
    private String workerGender;
    private String workerNumberOfKids;
    private String workerMotherMaidenName;
    private String workerGrandmotherMaidenName;
    private String contractSignatureDate;
    private String contractWorkStartDate;
    private String contractType;
    private String contractEndDate;
    private String contractWorkerId;
    private String payAmount;
    private String payCurrency;
    private String payEffectiveFrom;
    private String payEffectiveTo;
    private String compensationAmount;
    private String compensationType;
    private String compensationCurrency;
    private String compensationEffectiveFrom;
    private String compensationEffectiveTo;


    public boolean hasField(String fieldName) {

        switch (fieldName.toLowerCase()) {
            case "systemid":
            case "action":
            case "worker_name":
            case "worker_personalcode":
            case "worker_gender":
            case "worker_numberofkids":
            case "worker_mothermaidenname":
            case "worker_grandmothermaidenname":
            case "contract_signaturedate":
            case "contract_workstartdate":
            case "contract_type":
            case "contract_enddate":
            case "contract_workerid":
            case "pay_amount":
            case "pay_currency":
            case "pay_effectivefrom":
            case "pay_effectiveto":
            case "compensation_amount":
            case "compensation_type":
            case "compensation_currency":
            case "compensation_effectivefrom":
            case "compensation_effectiveto":
                return true;
            default:
                return false;
        }
    }

    public String getFieldValue(String fieldName) {

        switch (fieldName.toLowerCase()) {
            case "systemid": return systemId;
            case "action": return action;
            case "worker_name": return workerName;
            case "worker_personalcode": return workerPersonalCode;
            case "worker_gender": return workerGender;
            case "worker_numberofkids": return workerNumberOfKids;
            case "worker_mothermaidenname": return workerMotherMaidenName;
            case "worker_grandmothermaidenname": return workerGrandmotherMaidenName;
            case "contract_signaturedate": return contractSignatureDate;
            case "contract_workstartdate": return contractWorkStartDate;
            case "contract_type": return contractType;
            case "contract_enddate": return contractEndDate;
            case "contract_workerid": return contractWorkerId;
            case "pay_amount": return payAmount;
            case "pay_currency": return payCurrency;
            case "pay_effectivefrom": return payEffectiveFrom;
            case "pay_effectiveto": return payEffectiveTo;
            case "compensation_amount": return compensationAmount;
            case "compensation_type": return compensationType;
            case "compensation_currency": return compensationCurrency;
            case "compensation_effectivefrom": return compensationEffectiveFrom;
            case "compensation_effectiveto": return compensationEffectiveTo;
            default: return null; // Field not found
        }
    }
}
