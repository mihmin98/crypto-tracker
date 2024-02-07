package com.mihmin98.cryptotrackerbackend.enums;

import java.util.Arrays;
import java.util.List;

public enum CurrencyEnum {
    USD,
    USDT,
    BUSD,
    RON,
    BTC,
    ETH;

    public static final List<String> currencyNamesList = Arrays.stream(CurrencyEnum.values())
            .map(Enum::name)
            .toList();
}
