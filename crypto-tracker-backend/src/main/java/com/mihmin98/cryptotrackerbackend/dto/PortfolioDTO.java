package com.mihmin98.cryptotrackerbackend.dto;

import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import com.mihmin98.cryptotrackerbackend.model.TrackedCurrency;

import java.util.ArrayList;
import java.util.List;

public record PortfolioDTO(Long id, String name, List<TrackedCurrencyDTO> trackedCurrencies) {

    public static PortfolioDTO of(Portfolio portfolio, List<TrackedCurrency> trackedCurrencies) {
        List<TrackedCurrencyDTO> trackedCurrencyDTOS = trackedCurrencies.stream()
                .map(TrackedCurrencyDTO::from)
                .toList();
        return new PortfolioDTO(portfolio.getId(), portfolio.getName(), trackedCurrencyDTOS);
    }

    public static PortfolioDTO from(Portfolio portfolio) {
        return new PortfolioDTO(portfolio.getId(), portfolio.getName(), new ArrayList<>());
    }
}
