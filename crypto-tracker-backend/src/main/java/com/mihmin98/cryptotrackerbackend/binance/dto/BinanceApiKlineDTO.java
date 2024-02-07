package com.mihmin98.cryptotrackerbackend.binance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigDecimal;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder()
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BinanceApiKlineDTO {

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
}
