package com.trialtask.inbank.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoanDecisionResponse(

        @Schema(example = "POSITIVE")
        String decision,

        @Schema(example = "6000")
        Integer approvedAmount,

        @Schema(example = "24")
        Integer approvedPeriodMonths,

        Integer requestedAmount,
        Integer requestedPeriodMonths
) {
}
