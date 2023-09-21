package com.mihmin98.cryptotrackerbackend.history;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.IntervalEnum;
import com.mihmin98.cryptotrackerbackend.history.util.HistoricalDataUtil;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HistoricalDataUtilTests {

    @Test
    void formatDateTest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        long date = sdf.parse("2023-09-18 00:00:00").getTime();
        assertEquals("2023-09-18", HistoricalDataUtil.formatDate(date));

        date = sdf.parse("2023-09-18 23:59:59").getTime();
        assertEquals("2023-09-18", HistoricalDataUtil.formatDate(date));
    }

    @Test
    void buildBinanceHistoricalDataUrlTest() throws ParseException {
        TradingPair pair = TradingPair.of(CurrencyEnum.BTC, CurrencyEnum.USDT);

        IntervalEnum interval = IntervalEnum.DAILY;
        GranularityEnum granularity = GranularityEnum.SIX_HOURS;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        long date = sdf.parse("2023-09-18").getTime();

        String builtUrl = HistoricalDataUtil.buildBinanceHistoricalDataUrl(pair, interval, granularity, date);
        assertEquals("https://data.binance.vision/data/spot/daily/klines/BTCUSDT/6h/BTCUSDT-6h-2023-09-18.zip",
                builtUrl);
    }
}
