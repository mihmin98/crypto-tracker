package com.mihmin98.cryptotrackerbackend.binance.enums;

public enum IntervalEnum {

    DAILY("daily"),
    MONTHLY("monthly");

    private final String value;

    IntervalEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
