package com.trialtask.inbank.dto.response;

public record LoanDecisionResponse(
        String decision,
        Integer approvedAmount,
        Integer approvedPeriodMonths,
        Integer requestedAmount,
        Integer requestedPeriodMonths
) {
}