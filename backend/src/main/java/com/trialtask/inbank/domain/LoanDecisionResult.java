package com.trialtask.inbank.domain;

public record LoanDecisionResult(
        Decision decision,
        Integer approvedAmount,
        Integer approvedPeriodMonths,
        Integer requestedAmount,
        Integer requestedPeriodMonths
) {
}
