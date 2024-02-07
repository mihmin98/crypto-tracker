package com.mihmin98.cryptotrackerbackend.repository;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.model.TimestampTradingPairId;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TradingPairPriceRepository extends JpaRepository<TradingPairPrice, TimestampTradingPairId> {

    @Query("SELECT p " +
            "FROM TradingPairPrice p " +
            "WHERE p.id.tradingPair.baseCurrency = :baseCurrency AND p.id.tradingPair.quoteCurrency = :quoteCurrency AND (p.id.timestamp BETWEEN :startTime AND :endTime) " +
            "ORDER BY p.id.timestamp ASC")
    List<TradingPairPrice> findByTradingPairBetweenTimestamps(CurrencyEnum baseCurrency, CurrencyEnum quoteCurrency, Timestamp startTime, Timestamp endTime);
}
