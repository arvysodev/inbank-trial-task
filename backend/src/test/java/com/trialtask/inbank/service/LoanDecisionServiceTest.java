package com.trialtask.inbank.service;

import com.trialtask.inbank.domain.Decision;
import com.trialtask.inbank.domain.LoanDecisionResult;
import com.trialtask.inbank.dto.request.LoanDecisionRequest;
import com.trialtask.inbank.exception.UnknownPersonalCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class LoanDecisionServiceTest {

    private final CreditProfileService creditProfileService = new CreditProfileService();
    private final LoanDecisionService loanDecisionService = new LoanDecisionService(creditProfileService);

    @Test
    @DisplayName("should return negative decision when person has debt")
    void decide_whenPersonHasDebt_shouldReturnNegativeDecision() {
        LoanDecisionRequest request = new LoanDecisionRequest("49002010965", 4000, 24);

        LoanDecisionResult result = loanDecisionService.decide(request);

        assertThat(result.decision()).isEqualTo(Decision.NEGATIVE);
        assertThat(result.approvedAmount()).isNull();
        assertThat(result.approvedPeriodMonths()).isNull();
        assertThat(result.requestedAmount()).isEqualTo(4000);
        assertThat(result.requestedPeriodMonths()).isEqualTo(24);
    }

    @ParameterizedTest(name = "personalCode={0}, period={1} -> approvedAmount={2}")
    @CsvSource({
            "49002010976, 24, 2400",
            "49002010987, 12, 3600",
            "49002010998, 12, 10000"
    })
    @DisplayName("should return positive decision for selected period when it is valid")
    void decide_whenSelectedPeriodIsValid_shouldReturnPositiveDecision(
            String personalCode,
            int loanPeriodMonths,
            int expectedApprovedAmount
    ) {
        LoanDecisionRequest request = new LoanDecisionRequest(personalCode, 4000, loanPeriodMonths);

        LoanDecisionResult result = loanDecisionService.decide(request);

        assertThat(result.decision()).isEqualTo(Decision.POSITIVE);
        assertThat(result.approvedAmount()).isEqualTo(expectedApprovedAmount);
        assertThat(result.approvedPeriodMonths()).isEqualTo(loanPeriodMonths);
        assertThat(result.requestedAmount()).isEqualTo(4000);
        assertThat(result.requestedPeriodMonths()).isEqualTo(loanPeriodMonths);
    }

    @ParameterizedTest(name = "personalCode={0}, requestedPeriod={1} -> approvedAmount={2}, approvedPeriod={3}")
    @CsvSource({
            "49002010976, 19, 2000, 20",
            "49002010987, 12, 3600, 12"
    })
    @DisplayName("should return nearest suitable alternative period when selected period is not valid")
    void decide_whenSelectedPeriodIsInvalid_shouldReturnNearestSuitableAlternativePeriod(
            String personalCode,
            int requestedPeriod,
            int expectedApprovedAmount,
            int expectedApprovedPeriod
    ) {
        LoanDecisionRequest request = new LoanDecisionRequest(personalCode, 4000, requestedPeriod);

        LoanDecisionResult result = loanDecisionService.decide(request);

        assertThat(result.decision()).isEqualTo(Decision.POSITIVE);
        assertThat(result.approvedAmount()).isEqualTo(expectedApprovedAmount);
        assertThat(result.approvedPeriodMonths()).isEqualTo(expectedApprovedPeriod);
        assertThat(result.requestedAmount()).isEqualTo(4000);
        assertThat(result.requestedPeriodMonths()).isEqualTo(requestedPeriod);
    }

    @Test
    @DisplayName("should cap approved amount at maximum allowed loan amount")
    void decide_whenCalculatedAmountExceedsMaximum_shouldCapAtMaximumAllowedAmount() {
        LoanDecisionRequest request = new LoanDecisionRequest("49002010998", 4000, 60);

        LoanDecisionResult result = loanDecisionService.decide(request);

        assertThat(result.decision()).isEqualTo(Decision.POSITIVE);
        assertThat(result.approvedAmount()).isEqualTo(10000);
        assertThat(result.approvedPeriodMonths()).isEqualTo(60);
    }

    @Test
    @DisplayName("should throw exception when personal code is unknown")
    void decide_whenPersonalCodeIsUnknown_shouldThrowException() {
        LoanDecisionRequest request = new LoanDecisionRequest("12345678901", 4000, 24);

        assertThatThrownBy(() -> loanDecisionService.decide(request))
                .isInstanceOf(UnknownPersonalCodeException.class)
                .hasMessage("Unknown personal code: 12345678901");
    }
}
