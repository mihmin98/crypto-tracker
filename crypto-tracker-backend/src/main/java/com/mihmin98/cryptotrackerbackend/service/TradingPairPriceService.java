package com.mihmin98.cryptotrackerbackend.service;

import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.binance.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.binance.service.BinanceDataService;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import com.mihmin98.cryptotrackerbackend.repository.TradingPairPriceRepository;
import com.mihmin98.cryptotrackerbackend.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TradingPairPriceService {

    @Autowired
    TradingPairPriceRepository tradingPairPriceRepository;

    @Autowired
    BinanceDataService binanceDataService;

    public List<TradingPairPrice> downloadHistoricalData(TradingPair pair, GranularityEnum granularity, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        long numDays = ChronoUnit.DAYS.between(startDateTime, endDateTime);
        List<ZonedDateTime> daysToDownload = new ArrayList<>();
        daysToDownload.add(startDateTime);
        for (long i = 0; i < numDays; i++) {
            ZonedDateTime nextDay = daysToDownload.getLast().plusDays(1);
            daysToDownload.addLast(nextDay);
        }

        List<TradingPairPrice> downloadedTradingPairPrices = daysToDownload.stream()
                .parallel()
                .map(day -> binanceDataService.downloadData(pair, IntervalEnum.DAILY, granularity, day))
                .flatMap(List::stream)
                .sorted(Comparator.comparing(tradingPairPrice -> tradingPairPrice.getId().getTimestamp()))
                .toList();

        tradingPairPriceRepository.saveAll(downloadedTradingPairPrices);
        return downloadedTradingPairPrices;
    }

    public List<TradingPairPrice> downloadHistoricalData(TradingPair pair, GranularityEnum granularityEnum, String startDateUTC, String endDateUTC) {
        ZonedDateTime startDateTime = TimeUtils.convertUTCDateToZonedDateTime(startDateUTC);
        ZonedDateTime endDateTime = TimeUtils.convertUTCDateToZonedDateTime(endDateUTC);

        return downloadHistoricalData(pair, granularityEnum, startDateTime, endDateTime);
    }

    public List<TradingPairPrice> getPriceHistory(TradingPair pair, GranularityEnum granularity, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        // Compute startDate aligned with granularity, might be a little earlier than startDateTime
        ZonedDateTime startBeginningOfDayDateTime = startDateTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime finalStartDateTime = ZonedDateTime.from(startBeginningOfDayDateTime);
        while (true) {
            ZonedDateTime next = finalStartDateTime.plusSeconds(granularity.getSeconds());
            if (next.isAfter(startDateTime)) {
                break;
            }
            finalStartDateTime = next;
        }

        // Compute required prices times
        List<ZonedDateTime> requiredTimes = new ArrayList<>();
        requiredTimes.addLast(finalStartDateTime);
        while (true) {
            ZonedDateTime next = requiredTimes.getLast().plusSeconds(granularity.getSeconds());
            requiredTimes.addLast(next);

            if (next.isEqual(endDateTime) || next.isAfter(endDateTime)) {
                break;
            }
        }

        // Retrieve prices from db
        List<TradingPairPrice> queryResult = tradingPairPriceRepository.findByTradingPairBetweenTimestamps(
                pair.getBaseCurrency(),
                pair.getQuoteCurrency(),
                Timestamp.from(requiredTimes.getFirst().toInstant()),
                Timestamp.from(requiredTimes.getLast().toInstant())
        );

        ZoneId zoneId = requiredTimes.getLast().getZone();
        List<ZonedDateTime> queryResultTimes = queryResult.stream()
                .map(tpp -> ZonedDateTime.ofInstant(tpp.getId().getTimestamp().toInstant(), zoneId))
                .toList();

        // Compute which prices were not found in the database
        List<ZonedDateTime> notFoundTimes = new ArrayList<>(requiredTimes);
        notFoundTimes.removeAll(queryResultTimes);

        // Download prices that are not in the database
        List<TradingPairPrice> downloadedPrices = notFoundTimes.stream()
                .map(t -> t.withHour(0).withMinute(0).withSecond(0))
                .distinct()
                .map(t -> downloadHistoricalData(pair, granularity, t, t))
                .flatMap(List::stream)
                .toList();

        Map<Instant, TradingPairPrice> pricesMap = new HashMap<>();
        queryResult.forEach(tpp -> pricesMap.put(tpp.getId().getTimestamp().toInstant(), tpp));
        downloadedPrices.forEach(tpp -> pricesMap.put(tpp.getId().getTimestamp().toInstant(), tpp));

        List<TradingPairPrice> result = requiredTimes.stream()
                .map(t -> pricesMap.get(t.toInstant()))
                .filter(Objects::nonNull)
                .toList();

        return result;
    }

    public List<TradingPairPrice> getPriceHistory(TradingPair pair, GranularityEnum granularity, String startDateUTC, String endDateUTC) {
        ZonedDateTime startDateTime = TimeUtils.convertUTCDateToZonedDateTime(startDateUTC);
        ZonedDateTime endDateTime = TimeUtils.convertUTCDateToZonedDateTime(endDateUTC);

        return getPriceHistory(pair, granularity, startDateTime, endDateTime);
    }
}
