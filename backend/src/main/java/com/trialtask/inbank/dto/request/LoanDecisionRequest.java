package com.trialtask.inbank.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record LoanDecisionRequest(

        @Schema(example = "49002010976")
        @NotBlank
        String personalCode,

        @Schema(example = "4000")
        @Min(2000)
        @Max(10000)
        int loanAmount,

        @Schema(example = "24")
        @Min(12)
        @Max(60)
        int loanPeriodMonths
) {
}
