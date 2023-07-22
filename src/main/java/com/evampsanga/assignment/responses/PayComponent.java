package com.evampsanga.assignment.responses;

import lombok.Data;

@Data
public class PayComponent {
    private double amount;
    private String currency;
    private String startDate;
    private String endDate;
}
