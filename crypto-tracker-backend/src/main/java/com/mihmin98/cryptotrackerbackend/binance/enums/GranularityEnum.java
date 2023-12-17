package com.mihmin98.cryptotrackerbackend.binance.enums;

public enum GranularityEnum {

    ONE_SECOND("1s"),
    ONE_MINUTE("1m"),
    THREE_MINUTES("3m"),
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    THIRTY_MINUTES("30m"),
    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    SIX_HOURS("6h"),
    EIGHT_HOURS("8h"),
    TWELVE_HOURS("12h"),
    ONE_DAY("1d"),
    THREE_DAYS("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1mo");

    private final String value;

    GranularityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public long getMillis() {
        return switch (this) {
            case ONE_SECOND -> 1000L;
            case ONE_MINUTE -> 60_000L;
            case THREE_MINUTES -> 180_000L;
            case FIVE_MINUTES -> 300_000L;
            case FIFTEEN_MINUTES -> 900_000L;
            case THIRTY_MINUTES -> 1_800_000L;
            case ONE_HOUR -> 3_600_000L;
            case TWO_HOURS -> 7_200_000L;
            case FOUR_HOURS -> 14_400_000L;
            case SIX_HOURS -> 21_600_000L;
            case EIGHT_HOURS -> 28_800_000L;
            case TWELVE_HOURS -> 43_200_000L;
            case ONE_DAY -> 86_400_000L;
            case THREE_DAYS -> 259_200_000L;
            case ONE_WEEK -> 604_800_000L;
            case ONE_MONTH -> 2_629_800_000L;
        };
    }

    public long getSeconds() {
        return getMillis() / 1000L;
    }
}
