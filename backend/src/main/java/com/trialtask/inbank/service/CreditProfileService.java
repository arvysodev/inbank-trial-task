package com.trialtask.inbank.service;

import com.trialtask.inbank.domain.CreditProfile;
import com.trialtask.inbank.exception.UnknownPersonalCodeException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CreditProfileService {

    private static final Map<String, CreditProfile> CREDIT_PROFILES = Map.of(
            "49002010965", new CreditProfile("49002010965", true, 0),
            "49002010976", new CreditProfile("49002010976", false, 100),
            "49002010987", new CreditProfile("49002010987", false, 300),
            "49002010998", new CreditProfile("49002010998", false, 1000)
    );

    /**
     * Retrieves the credit profile for a given personal code.
     *
     * @param personalCode unique identifier of the customer
     * @return credit profile containing debt status and credit modifier
     * @throws UnknownPersonalCodeException if no profile is found
     */
    public CreditProfile getCreditProfile(String personalCode) {
        CreditProfile profile = CREDIT_PROFILES.get(personalCode);

        if (profile == null) {
            throw new UnknownPersonalCodeException(personalCode);
        }

        return profile;
    }
}
