package com.mihmin98.cryptotrackerbackend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@Embeddable
public class TimestampTradingPairId implements Serializable {

    @Column
    private Timestamp timestamp;

    @Embedded
    private TradingPair tradingPair;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimestampTradingPairId that = (TimestampTradingPairId) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(tradingPair, that.tradingPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, tradingPair);
    }
}
