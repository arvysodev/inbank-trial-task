package com.trialtask.inbank.service;

import com.trialtask.inbank.domain.CreditProfile;
import com.trialtask.inbank.domain.Decision;
import com.trialtask.inbank.domain.LoanDecisionResult;
import com.trialtask.inbank.dto.request.LoanDecisionRequest;
import org.springframework.stereotype.Service;

@Service
public class LoanDecisionService {

    private static final int MIN_LOAN_AMOUNT = 2000;
    private static final int MAX_LOAN_AMOUNT = 10000;
    private static final int MIN_LOAN_PERIOD = 12;
    private static final int MAX_LOAN_PERIOD = 60;

    private final CreditProfileService creditProfileService;

    public LoanDecisionService(CreditProfileService creditProfileService) {
        this.creditProfileService = creditProfileService;
    }

    /**
     * Processes a loan decision request and returns the calculated result.
     * <p>
     * The decision flow:
     * <ul>
     *     <li>Retrieve customer's credit profile</li>
     *     <li>Reject if customer has active debt</li>
     *     <li>Attempt to calculate offer for requested period</li>
     *     <li>If not possible, search for best alternative period</li>
     * </ul>
     *
     * @param request loan decision request
     * @return loan decision result containing approval status and offer details
     */
    public LoanDecisionResult decide(LoanDecisionRequest request) {
        CreditProfile creditProfile = creditProfileService.getCreditProfile(request.personalCode());

        if (creditProfile.hasDebt()) {
            return negativeDecision(request);
        }

        LoanDecisionResult selectedPeriodDecision = evaluateSelectedPeriod(request, creditProfile);
        if (selectedPeriodDecision != null) {
            return selectedPeriodDecision;
        }

        LoanDecisionResult alternativePeriodDecision = findNearestAlternativePeriod(request, creditProfile);
        if (alternativePeriodDecision != null) {
            return alternativePeriodDecision;
        }

        return negativeDecision(request);
    }

    private LoanDecisionResult evaluateSelectedPeriod(LoanDecisionRequest request, CreditProfile creditProfile) {
        Integer approvedAmount = calculateApprovedAmount(creditProfile.creditModifier(), request.loanPeriodMonths());

        if (approvedAmount == null) {
            return null;
        }

        return new LoanDecisionResult(
                Decision.POSITIVE,
                approvedAmount,
                request.loanPeriodMonths(),
                request.loanAmount(),
                request.loanPeriodMonths()
        );
    }

    private LoanDecisionResult findNearestAlternativePeriod(
            LoanDecisionRequest request,
            CreditProfile creditProfile
    ) {
        for (int period = request.loanPeriodMonths() + 1; period <= MAX_LOAN_PERIOD; period++) {
            Integer approvedAmount = calculateApprovedAmount(creditProfile.creditModifier(), period);

            if (approvedAmount == null) {
                continue;
            }

            return new LoanDecisionResult(
                    Decision.POSITIVE,
                    approvedAmount,
                    period,
                    request.loanAmount(),
                    request.loanPeriodMonths()
            );
        }

        return null;
    }

    private Integer calculateApprovedAmount(int creditModifier, int loanPeriodMonths) {
        int maximumApprovedAmount = Math.min(MAX_LOAN_AMOUNT, creditModifier * loanPeriodMonths);

        if (maximumApprovedAmount < MIN_LOAN_AMOUNT) {
            return null;
        }

        return maximumApprovedAmount;
    }

    private LoanDecisionResult negativeDecision(LoanDecisionRequest request) {
        return new LoanDecisionResult(
                Decision.NEGATIVE,
                null,
                null,
                request.loanAmount(),
                request.loanPeriodMonths()
        );
    }
}
