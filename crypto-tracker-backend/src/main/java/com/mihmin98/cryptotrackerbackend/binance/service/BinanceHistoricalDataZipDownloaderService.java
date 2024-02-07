package com.mihmin98.cryptotrackerbackend.binance.service;

import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.binance.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.binance.util.BinanceDataUtils;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.mihmin98.cryptotrackerbackend.binance.util.BinanceDataUtils.buildBinanceHistoricalDataUrl;
import static com.mihmin98.cryptotrackerbackend.binance.util.BinanceDataUtils.formatDate;

@Service
public class BinanceHistoricalDataZipDownloaderService {

    public byte[] downloadBinanceHistoricalDataZip(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startTimeMillis) {
        String url = buildBinanceHistoricalDataUrl(pair, interval, granularity, startTimeMillis);
        try {
            return BinanceDataUtils.downloadByteArrayFromUrl(new URI(url).toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> extractCsvLinesFromZip(byte[] zipFileByteArray, TradingPair pair, GranularityEnum granularity, long startTimeMillis) {
        try (ByteArrayInputStream zipByteStream = new ByteArrayInputStream(zipFileByteArray)) {
            ZipInputStream zipStream = new ZipInputStream(zipByteStream);
            ZipEntry zipEntry = zipStream.getNextEntry();

            if (zipEntry == null) {
                throw new IllegalStateException("ZIP file has no entries");
            }

            String csvFilename = String.format("%s-%s-%s.csv",
                    pair.getBaseCurrency().toString() + pair.getQuoteCurrency().toString(), granularity.getValue(), formatDate(startTimeMillis));

            if (!zipEntry.getName().equals(csvFilename)) {
                throw new IllegalStateException("ZIP file does not contain file " + csvFilename);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(zipStream);
            CSVReader csvReader = new CSVReader(inputStreamReader);

            return csvReader.readAll();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }
}
