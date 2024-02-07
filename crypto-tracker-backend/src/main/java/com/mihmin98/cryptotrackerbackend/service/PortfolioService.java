package com.mihmin98.cryptotrackerbackend.service;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import com.mihmin98.cryptotrackerbackend.model.TrackedCurrency;
import com.mihmin98.cryptotrackerbackend.repository.PortfolioRepository;
import com.mihmin98.cryptotrackerbackend.repository.TrackedCurrencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    TrackedCurrencyRepository trackedCurrencyRepository;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Transactional
    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public void deletePortfolio(Long id) {
        trackedCurrencyRepository.deleteAllTrackedCurrenciesFromPortfolio(id);
        portfolioRepository.deleteById(id);
    }

    @Transactional
    public TrackedCurrency addTrackedCurrencyToPortfolio(CurrencyEnum currency, Portfolio portfolio) throws IllegalArgumentException{
        // Workaround since Hibernate can't add UNIQUE constraints for multiple columns to SQLite
        TrackedCurrency existingTrackedCurrency = trackedCurrencyRepository.findTrackedCurrencyInPortfolio(currency, portfolio.getId());
        if (existingTrackedCurrency != null) {
            throw new IllegalArgumentException("Currency " + currency.name() + " already present in portfolio with id " + portfolio.getId());
        }

        TrackedCurrency trackedCurrency = TrackedCurrency.of(currency);
        trackedCurrency.setPortfolio(portfolio);
        return trackedCurrencyRepository.save(trackedCurrency);
    }

    @Transactional
    public TrackedCurrency addTrackedCurrencyToPortfolio(CurrencyEnum currency, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.getReferenceById(portfolioId);
        return addTrackedCurrencyToPortfolio(currency, portfolio);
    }

    @Transactional
    public void removeTrackedCurrencyFromPortfolio(CurrencyEnum currency, Long portfolioId) {
        TrackedCurrency trackedCurrency = trackedCurrencyRepository.findTrackedCurrencyInPortfolio(currency, portfolioId);
        trackedCurrencyRepository.delete(trackedCurrency);
    }

    @Transactional
    public void removeTrackedCurrencyFromPortfolio(CurrencyEnum currency, Portfolio portfolio) {
        removeTrackedCurrencyFromPortfolio(currency, portfolio.getId());
    }

    public List<TrackedCurrency> getPortfolioTrackedCurrencies(Long portfolioId) {
        return trackedCurrencyRepository.findTrackedCurrenciesByPortfolio(portfolioId);
    }

    public List<TrackedCurrency> getPortfolioTrackedCurrencies(Portfolio portfolio) {
        return getPortfolioTrackedCurrencies(portfolio.getId());
    }
}
