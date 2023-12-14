package com.mihmin98.cryptotrackerbackend.binance.util;

import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.binance.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

public class BinanceDataUtils {

    public static String formatDate(long dateMillis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(dateMillis);

        return String.format("%tF", calendar);
    }

    public static String buildBinanceHistoricalDataUrl(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startTimeMillis) {
        String dateString = formatDate(startTimeMillis);
        String symbol = pair.getBaseCurrency().toString() + pair.getQuoteCurrency().toString();

        return String.format("https://data.binance.vision/data/spot/%s/klines/%s/%s/%s-%s-%s.zip",
                interval.getValue(), symbol, granularity.getValue(), symbol, granularity.getValue(), dateString);
    }

    public static byte[] downloadByteArrayFromUrl(URL url) {
        try (BufferedInputStream in = new BufferedInputStream(url.openStream())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = in.readNBytes(data, 0, data.length)) != 0) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
