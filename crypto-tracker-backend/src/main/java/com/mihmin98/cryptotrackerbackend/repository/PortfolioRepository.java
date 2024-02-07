package com.mihmin98.cryptotrackerbackend.repository;

import com.mihmin98.cryptotrackerbackend.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
