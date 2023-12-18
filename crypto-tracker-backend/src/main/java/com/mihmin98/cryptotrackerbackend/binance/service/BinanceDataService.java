package com.mihmin98.cryptotrackerbackend.binance.service;

import com.mihmin98.cryptotrackerbackend.binance.dto.BinanceApiKlineDTO;
import com.mihmin98.cryptotrackerbackend.binance.dto.BinanceHistoricalKlineCsvDTO;
import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.binance.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BinanceDataService {

    @Autowired
    private BinanceHistoricalDataZipDownloaderService historicalDataDownloaderService;

    @Autowired
    private BinanceApiDataDownloaderService apiDataDownloaderService;

    public List<TradingPairPrice> downloadHistoricalData(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startTimeMillis) {
        // TODO: maybe return empty list on error? like FileNotFound
        byte[] zipFileByteArray = historicalDataDownloaderService.downloadBinanceHistoricalDataZip(pair, interval, granularity, startTimeMillis);
        List<String[]> csvLines = historicalDataDownloaderService.extractCsvLinesFromZip(zipFileByteArray, pair, granularity, startTimeMillis);

        List<BinanceHistoricalKlineCsvDTO> dtoList = csvLines.stream()
                .map(BinanceHistoricalKlineCsvDTO::from)
                .toList();

        return dtoList.stream()
                .map(dto -> TradingPairPrice.of(dto, pair))
                .toList();
    }

    public List<TradingPairPrice> downloadApiData(TradingPair pair, GranularityEnum granularity, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        // TODO: Add case where more than 1000 entries would be returned
        byte[] klinesJsonByteArray = apiDataDownloaderService.downloadApiDataByteArray(pair, granularity, startDateTime, endDateTime);
        List<BinanceApiKlineDTO> dtoList = apiDataDownloaderService.extractKlinesFromByteArray(klinesJsonByteArray);

        return dtoList.stream()
                .map(dto -> TradingPairPrice.of(dto, pair))
                .toList();
    }

    public List<TradingPairPrice> downloadData(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, ZonedDateTime startDateTime) {
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
        long daysDiff = ChronoUnit.DAYS.between(startDateTime, currentTime);
        long startTimeMillis = startDateTime.toInstant().toEpochMilli();

        if (daysDiff >= 7) {
            return downloadHistoricalData(pair, interval, granularity, startTimeMillis);
        } else {
            ZonedDateTime endDateTime = startDateTime.plusDays(1);
            return downloadApiData(pair, granularity, startDateTime, endDateTime);
        }
    }
}
