package com.mihmin98.cryptotrackerbackend.binance.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BinanceHistoricalKlineCsvDTO {

    private long openTimestamp;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private long closeTimestamp;
    private BigDecimal quoteVolume;
    private long numTrades;
    private BigDecimal takerBuyVolume;
    private BigDecimal takerBuyQuoteVolume;
    private int ignore;

    private BinanceHistoricalKlineCsvDTO() {

    }

    static public BinanceHistoricalKlineCsvDTO from(String[] csvStringArray) {
        BinanceHistoricalKlineCsvDTO dto = new BinanceHistoricalKlineCsvDTO();

        dto.openTimestamp = Long.parseLong(csvStringArray[0]);
        dto.openPrice = new BigDecimal(csvStringArray[1]);
        dto.highPrice = new BigDecimal(csvStringArray[2]);
        dto.lowPrice = new BigDecimal(csvStringArray[3]);
        dto.closePrice = new BigDecimal(csvStringArray[4]);
        dto.volume = new BigDecimal(csvStringArray[5]);
        dto.closeTimestamp = Long.parseLong(csvStringArray[6]);
        dto.quoteVolume = new BigDecimal(csvStringArray[7]);
        dto.numTrades = Long.parseLong(csvStringArray[8]);
        dto.takerBuyVolume = new BigDecimal(csvStringArray[9]);
        dto.takerBuyQuoteVolume = new BigDecimal(csvStringArray[10]);
        dto.ignore = Integer.parseInt(csvStringArray[11]);

        return dto;
    }
}
