package com.mihmin98.cryptotrackerbackend.controller;

import com.mihmin98.cryptotrackerbackend.dto.PortfolioCreateDTO;
import com.mihmin98.cryptotrackerbackend.dto.PortfolioDTO;
import com.mihmin98.cryptotrackerbackend.dto.PortfolioUpdateDTO;
import com.mihmin98.cryptotrackerbackend.dto.TrackedCurrencyDTO;
import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import com.mihmin98.cryptotrackerbackend.model.TrackedCurrency;
import com.mihmin98.cryptotrackerbackend.service.PortfolioService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("")
    public ResponseEntity<?> getAllPortfolios(@RequestParam(required = false) Boolean fetchTrackedCurrencies) {
        List<Portfolio> portfolioList = portfolioService.getAllPortfolios();
        List<PortfolioDTO> portfolioDTOS;
        if (BooleanUtils.isTrue(fetchTrackedCurrencies)) {
            portfolioDTOS = portfolioList.stream()
                    .map(p -> PortfolioDTO.of(p, portfolioService.getPortfolioTrackedCurrencies(p)))
                    .toList();
        } else {
            portfolioDTOS = portfolioList.stream()
                    .map(PortfolioDTO::from)
                    .toList();
        }
        return new ResponseEntity<>(portfolioDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolio(@PathVariable("id") Long id) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioById(id);
        if (portfolio.isPresent()) {
            List<TrackedCurrency> trackedCurrencies = portfolioService.getPortfolioTrackedCurrencies(id);
            PortfolioDTO dto = PortfolioDTO.of(portfolio.get(), trackedCurrencies);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createPortfolio(@Valid @RequestBody PortfolioCreateDTO portfolioDTO) {
        Portfolio portfolio = portfolioService.savePortfolio(Portfolio.of(portfolioDTO));
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> updatePortfolio(@Valid @RequestBody PortfolioUpdateDTO portfolioDTO) {
        Optional<Portfolio> optPortfolio = portfolioService.getPortfolioById(portfolioDTO.id());
        if (optPortfolio.isPresent()) {
            Portfolio portfolio = optPortfolio.get();
            portfolio.setValues(portfolioDTO);
            return new ResponseEntity<>(portfolioService.savePortfolio(portfolio), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePortfolio(@PathVariable Long id) {
        if (portfolioService.getPortfolioById(id).isPresent()) {
            portfolioService.deletePortfolio(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/currencies")
    public ResponseEntity<?> addCurrencyToPortfolio(@PathVariable Long id, @RequestParam(name = "currency") String currency) {
        if (!CurrencyEnum.currencyNamesList.contains(currency)) {
            return new ResponseEntity<>(currency + " is not a valid currency", HttpStatus.BAD_REQUEST);
        }

        try {
            TrackedCurrency trackedCurrency = portfolioService.addTrackedCurrencyToPortfolio(CurrencyEnum.valueOf(currency), id);
            return new ResponseEntity<>(TrackedCurrencyDTO.from(trackedCurrency), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}/currencies")
    public ResponseEntity<?> removeCurrencyFromPortfolio(@PathVariable Long id, @RequestParam(name = "currency") String currency) {
        if (!CurrencyEnum.currencyNamesList.contains(currency)) {
            return new ResponseEntity<>(currency + " is not a valid currency", HttpStatus.BAD_REQUEST);
        }

        portfolioService.removeTrackedCurrencyFromPortfolio(CurrencyEnum.valueOf(currency), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
