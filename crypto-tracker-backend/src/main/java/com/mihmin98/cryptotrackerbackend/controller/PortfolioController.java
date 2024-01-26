package com.mihmin98.cryptotrackerbackend.controller;

import com.mihmin98.cryptotrackerbackend.dto.PortfolioCreateDTO;
import com.mihmin98.cryptotrackerbackend.dto.PortfolioUpdateDTO;
import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import com.mihmin98.cryptotrackerbackend.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("")
    public ResponseEntity<?> getAllPortfolios() {
        return new ResponseEntity<>(portfolioService.getAllPortfolios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPortfolio(@PathVariable("id") Long id) {
        Optional<Portfolio> portfolio = portfolioService.getById(id);
        if (portfolio.isPresent()) {
            return new ResponseEntity<>(portfolio.get(), HttpStatus.OK);
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
        Optional<Portfolio> optPortfolio = portfolioService.getById(portfolioDTO.id());
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
        if (portfolioService.getById(id).isPresent()) {
            portfolioService.deletePortfolio(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
