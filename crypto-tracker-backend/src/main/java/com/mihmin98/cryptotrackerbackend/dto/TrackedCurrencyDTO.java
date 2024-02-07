package com.mihmin98.cryptotrackerbackend.dto;

import com.mihmin98.cryptotrackerbackend.model.TrackedCurrency;

public record TrackedCurrencyDTO(String currency) {

    public static TrackedCurrencyDTO from(TrackedCurrency trackedCurrency) {
        return new TrackedCurrencyDTO(trackedCurrency.getCurrency().name());
    }
}
