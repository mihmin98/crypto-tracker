package com.mihmin98.cryptotrackerbackend.history;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.history.service.BinanceHistoricalDataZipDownloaderService;
import com.mihmin98.cryptotrackerbackend.history.service.HistoricalDataService;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HistoricalDataServiceTests {

    @Autowired
    private HistoricalDataService historicalDataService;

    @Mock
    private BinanceHistoricalDataZipDownloaderService zipDownloaderServiceMock;

    @Test
    void downloadHistoricalDataTest() {
        TradingPair tradingPairBTCUSDT = TradingPair.of(CurrencyEnum.BTC, CurrencyEnum.USDT);

        // TODO: Add values in a config file
        List<String[]> mockedCsvLines = new ArrayList<>();
        mockedCsvLines.add(new String[]{"1694995200000", "26527.50000000", "26750.00000000", "26377.35000000", "26647.45000000", "6442.08445000", "1695016799999", "171250105.76290800", "171987", "3164.69751000", "84135842.58948540", "0"});
        mockedCsvLines.add(new String[]{"1695016800000", "26647.45000000", "27240.00000000", "26622.77000000", "27196.98000000", "12659.48973000", "1695038399999", "341101054.80983600", "267902", "6702.79322000", "180628182.04775770", "0"});

        List<TradingPairPrice> expectedTradingPairPriceList = new ArrayList<>();
        expectedTradingPairPriceList.add(TradingPairPrice.of(new Timestamp(1694995200000L), tradingPairBTCUSDT, new BigDecimal("26527.50000000")));
        expectedTradingPairPriceList.add(TradingPairPrice.of(new Timestamp(1695016800000L), tradingPairBTCUSDT, new BigDecimal("26647.45000000")));

        ReflectionTestUtils.setField(historicalDataService, "zipDownloaderService", zipDownloaderServiceMock);
        doReturn(null).when(zipDownloaderServiceMock).downloadBinanceHistoricalDataZip(any(), any(), any(), anyLong());
        doReturn(mockedCsvLines).when(zipDownloaderServiceMock).extractCsvLinesFromZip(any(), eq(tradingPairBTCUSDT), any(), anyLong());

        List<TradingPairPrice> returnedTradingPairPriceList = historicalDataService.downloadHistoricalData(tradingPairBTCUSDT, null, null, 0L);

        assertEquals(expectedTradingPairPriceList.size(), returnedTradingPairPriceList.size());
        for (int i = 0; i < expectedTradingPairPriceList.size(); ++i) {
            TradingPairPrice expected = expectedTradingPairPriceList.get(i);
            TradingPairPrice actual = returnedTradingPairPriceList.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getPrice(), actual.getPrice());
        }
    }
}
