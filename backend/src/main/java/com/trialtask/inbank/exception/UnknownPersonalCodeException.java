package com.trialtask.inbank.exception;

public class UnknownPersonalCodeException extends RuntimeException {

    public UnknownPersonalCodeException(String personalCode) {
        super("Unknown personal code: " + personalCode);
    }
}
