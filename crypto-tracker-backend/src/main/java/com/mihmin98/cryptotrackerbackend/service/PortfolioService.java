package com.mihmin98.cryptotrackerbackend.service;

import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import com.mihmin98.cryptotrackerbackend.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    public Optional<Portfolio> getById(Long id) {
        return portfolioRepository.findById(id);
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long id) {
        // TODO: when deleting portfolio also delete transactions related to portfolio
        portfolioRepository.deleteById(id);
    }
}
