package com.mihmin98.cryptotrackerbackend.history.service;

import com.mihmin98.cryptotrackerbackend.history.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.IntervalEnum;
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

import static com.mihmin98.cryptotrackerbackend.history.util.HistoricalDataUtil.buildBinanceHistoricalDataUrl;
import static com.mihmin98.cryptotrackerbackend.history.util.HistoricalDataUtil.formatDate;

@Service
public class BinanceHistoricalDataZipDownloaderService {

    public byte[] downloadBinanceHistoricalDataZip(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startDate) {
        String url = buildBinanceHistoricalDataUrl(pair, interval, granularity, startDate);
        byte[] zipFileByteArray;

        try (BufferedInputStream in = new BufferedInputStream(new URI(url).toURL().openStream())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = in.readNBytes(data, 0, data.length)) != 0) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            zipFileByteArray = buffer.toByteArray();

            return zipFileByteArray;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> extractCsvLinesFromZip(byte[] zipFileByteArray, TradingPair pair, GranularityEnum granularity, long startDate) {
        try (ByteArrayInputStream zipByteStream = new ByteArrayInputStream(zipFileByteArray)) {
            ZipInputStream zipStream = new ZipInputStream(zipByteStream);
            ZipEntry zipEntry = zipStream.getNextEntry();

            if (zipEntry == null) {
                throw new IllegalStateException("ZIP file has no entries");
            }

            String csvFilename = String.format("%s-%s-%s.csv",
                    pair.getBaseCurrency().toString() + pair.getQuoteCurrency().toString(), granularity.getValue(), formatDate(startDate));

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
