package com.sgg.test.controller.model;

import lombok.Data;

@Data
public class ViolationSummaryResponse {
    private Integer totalPaidFines = 0;
    private Integer totalUnpaidFines = 0;
}
