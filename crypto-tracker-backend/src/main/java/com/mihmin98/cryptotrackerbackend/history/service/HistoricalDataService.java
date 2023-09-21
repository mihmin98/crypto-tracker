package com.mihmin98.cryptotrackerbackend.history.service;

import com.mihmin98.cryptotrackerbackend.history.dto.BinanceHistoricalKlineCsvDTO;
import com.mihmin98.cryptotrackerbackend.history.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricalDataService {

    @Autowired
    private BinanceHistoricalDataZipDownloaderService zipDownloaderService;

    public List<TradingPairPrice> downloadHistoricalData(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startDate) {
        byte[] zipFileByteArray = zipDownloaderService.downloadBinanceHistoricalDataZip(pair, interval, granularity, startDate);
        List<String[]> csvLines = zipDownloaderService.extractCsvLinesFromZip(zipFileByteArray, pair, granularity, startDate);

        List<BinanceHistoricalKlineCsvDTO> dtoList = csvLines.stream()
                .map(BinanceHistoricalKlineCsvDTO::from)
                .toList();

        return dtoList.stream()
                .map(dto -> TradingPairPrice.of(dto, pair))
                .toList();
    }


}
