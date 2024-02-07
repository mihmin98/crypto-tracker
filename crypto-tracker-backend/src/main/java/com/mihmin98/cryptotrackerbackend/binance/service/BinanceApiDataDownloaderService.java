package com.mihmin98.cryptotrackerbackend.binance.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mihmin98.cryptotrackerbackend.binance.dto.BinanceApiKlineDTO;
import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.binance.util.BinanceDataUtils;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class BinanceApiDataDownloaderService {

    public static final String BINANCE_API_BASE_ENDPOINT = "https://api.binance.com";
    public static final String BINANCE_KLINES_ENDPOINT = BINANCE_API_BASE_ENDPOINT + "/api/v3/klines";
    public static final String SYMBOL_QUERY_PARAM = "symbol";
    public static final String INTERVAL_QUERY_PARAM = "interval";
    public static final String START_TIME_QUERY_PARAM = "startTime";
    public static final String END_TIME_QUERY_PARAM = "endTime";
    public static final String TIME_ZONE_QUERY_PARAM = "timeZone";
    public static final String LIMIT_QUERY_PARAM = "limit";

    public byte[] downloadApiDataByteArray(TradingPair pair, GranularityEnum granularity, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        String tradingPairStr = pair.getBaseCurrency().toString() + pair.getQuoteCurrency().toString();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BINANCE_KLINES_ENDPOINT)
                .queryParam(SYMBOL_QUERY_PARAM, tradingPairStr)
                .queryParam(INTERVAL_QUERY_PARAM, granularity.getValue())
                .queryParam(START_TIME_QUERY_PARAM, startDateTime.toInstant().toEpochMilli());

        if (endDateTime != null) {
            uriBuilder.queryParam(END_TIME_QUERY_PARAM, endDateTime.toInstant().toEpochMilli());
        }

        URI uri = uriBuilder.build().toUri();

        try {
            return BinanceDataUtils.downloadByteArrayFromUrl(uri.toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinanceApiKlineDTO> extractKlinesFromByteArray(byte[] klinesByteArray) {
        ObjectMapper objectMapper = new ObjectMapper();
        String klinesString = new String(klinesByteArray);
        TypeReference<List<BinanceApiKlineDTO>> typeReference = new TypeReference<>() {};
        try {
            return objectMapper.readValue(klinesString, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
