package com.trialtask.inbank.domain;

public record CreditProfile(
        String personalCode,
        boolean hasDebt,
        int creditModifier
) {
}
