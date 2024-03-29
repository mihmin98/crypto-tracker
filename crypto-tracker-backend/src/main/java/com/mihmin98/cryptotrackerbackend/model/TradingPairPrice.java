package com.mihmin98.cryptotrackerbackend.model;

import com.mihmin98.cryptotrackerbackend.binance.dto.BinanceApiKlineDTO;
import com.mihmin98.cryptotrackerbackend.binance.dto.BinanceHistoricalKlineCsvDTO;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "trading_pairs_prices")
@Getter
@Setter
public class TradingPairPrice {

    @EmbeddedId
    private TimestampTradingPairId id;

    @Column(precision = 14, scale = 8)
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradingPairPrice priceInfo = (TradingPairPrice) o;
        return Objects.equals(id, priceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static TradingPairPrice of(BinanceHistoricalKlineCsvDTO dto, TradingPair tradingPair) {
        TradingPairPrice tradingPairPrice = new TradingPairPrice();

        TimestampTradingPairId id = new TimestampTradingPairId();
        id.setTradingPair(tradingPair);
        id.setTimestamp(new Timestamp(dto.getOpenTimestamp()));

        tradingPairPrice.setId(id);
        tradingPairPrice.setPrice(dto.getOpenPrice());

        return tradingPairPrice;
    }

    public static TradingPairPrice of(BinanceApiKlineDTO dto, TradingPair tradingPair) {
        TradingPairPrice tradingPairPrice = new TradingPairPrice();

        TimestampTradingPairId id = new TimestampTradingPairId();
        id.setTradingPair(tradingPair);
        id.setTimestamp(new Timestamp(dto.getOpenTimestamp()));

        tradingPairPrice.setId(id);
        tradingPairPrice.setPrice(dto.getOpenPrice());

        return tradingPairPrice;
    }

    public static TradingPairPrice of(Timestamp timestamp, TradingPair tradingPair, BigDecimal price) {
        TradingPairPrice tradingPairPrice = new TradingPairPrice();

        TimestampTradingPairId id = new TimestampTradingPairId();
        id.setTimestamp(timestamp);
        id.setTradingPair(tradingPair);

        tradingPairPrice.setId(id);
        tradingPairPrice.setPrice(price);

        return tradingPairPrice;
    }
}
