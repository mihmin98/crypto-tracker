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

    public static TradingPair of(String pair) {
        String[] splitPair = pair.split("/");
        if (splitPair.length != 2) {
            throw new IllegalArgumentException("Given pair was not in the format \"baseCurrency/quoteCurrency\"");
        }

        CurrencyEnum baseCurrency = CurrencyEnum.valueOf(splitPair[0]);
        CurrencyEnum quoteCurrency = CurrencyEnum.valueOf(splitPair[1]);
        return of(baseCurrency, quoteCurrency);
    }
}
