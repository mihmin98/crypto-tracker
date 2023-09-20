package com.mihmin98.cryptotrackerbackend.model;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class TradingPair implements Serializable {

    @Column(name = "base_currency")
    private CurrencyEnum baseCurrency;

    @Column(name = "quote_currency")
    private CurrencyEnum quoteCurrency;

    @Override
    public String toString() {
        return baseCurrency.name() + "/" + quoteCurrency.name();
    }
}
