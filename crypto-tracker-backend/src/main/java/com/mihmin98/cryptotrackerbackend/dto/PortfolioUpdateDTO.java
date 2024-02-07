package com.mihmin98.cryptotrackerbackend.dto;

import jakarta.validation.constraints.NotNull;

public record PortfolioUpdateDTO(@NotNull Long id, String name) {
}
