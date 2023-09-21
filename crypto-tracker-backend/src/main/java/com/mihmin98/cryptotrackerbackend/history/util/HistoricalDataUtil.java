package com.mihmin98.cryptotrackerbackend.history.util;

import com.mihmin98.cryptotrackerbackend.history.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;

import java.util.Calendar;
import java.util.TimeZone;

public class HistoricalDataUtil {

    public static String formatDate(long date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(date);

        return String.format("%tF", calendar);
    }

    public static String buildBinanceHistoricalDataUrl(TradingPair pair, IntervalEnum interval, GranularityEnum granularity, long startDate) {
        String dateString = formatDate(startDate);
        String symbol = pair.getBaseCurrency().toString() + pair.getQuoteCurrency().toString();

        return String.format("https://data.binance.vision/data/spot/%s/klines/%s/%s/%s-%s-%s.zip",
                interval.getValue(), symbol, granularity.getValue(), symbol, granularity.getValue(), dateString);
    }
}
