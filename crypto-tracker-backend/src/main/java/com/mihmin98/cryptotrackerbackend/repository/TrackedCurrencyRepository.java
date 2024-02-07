package com.mihmin98.cryptotrackerbackend.repository;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.model.TrackedCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrackedCurrencyRepository extends JpaRepository<TrackedCurrency, Long> {

    @Query("SELECT tc " +
            "FROM TrackedCurrency tc " +
            "WHERE tc.portfolio.id = :portfolioId")
    List<TrackedCurrency> findTrackedCurrenciesByPortfolio(Long portfolioId);

    @Query("SELECT tc " +
            "FROM TrackedCurrency tc " +
            "WHERE tc.portfolio.id = :portfolioId AND tc.currency = :currency")
    TrackedCurrency findTrackedCurrencyInPortfolio(CurrencyEnum currency, Long portfolioId);

    @Modifying
    @Query("DELETE " +
            "FROM TrackedCurrency tc " +
            "WHERE tc.portfolio.id = :portfolioId")
    void deleteAllTrackedCurrenciesFromPortfolio(Long portfolioId);
}
