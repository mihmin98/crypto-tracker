package com.mihmin98.cryptotrackerbackend.model;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    public static TradingPair of(CurrencyEnum baseCurrency, CurrencyEnum quoteCurrency) {
        return new TradingPair(baseCurrency, quoteCurrency);
    }
}
