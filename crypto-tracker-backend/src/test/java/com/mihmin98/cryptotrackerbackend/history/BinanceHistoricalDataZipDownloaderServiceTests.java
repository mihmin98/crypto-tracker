package com.mihmin98.cryptotrackerbackend.history;

import com.mihmin98.cryptotrackerbackend.enums.CurrencyEnum;
import com.mihmin98.cryptotrackerbackend.history.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.history.service.BinanceHistoricalDataZipDownloaderService;
import com.mihmin98.cryptotrackerbackend.history.util.HistoricalDataUtil;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BinanceHistoricalDataZipDownloaderServiceTests {

    @Autowired
    BinanceHistoricalDataZipDownloaderService zipDownloaderService;

    @Test
    void downloadBinanceHistoricalDataZipTest() throws IOException {
        byte[] mockByteArray = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        InputStream inputStream = new ByteArrayInputStream(mockByteArray);
        URL urlMock = mock(URL.class);

        try (MockedConstruction<URI> uriMockedConstruction = mockConstruction(URI.class, (mock, context) ->
                doReturn(urlMock).when(mock).toURL());
             MockedStatic<HistoricalDataUtil> utilMock = mockStatic(HistoricalDataUtil.class)) {

            utilMock.when(() -> HistoricalDataUtil.buildBinanceHistoricalDataUrl(any(), any(), any(), anyLong())).thenReturn("testUrl");
            doReturn(inputStream).when(urlMock).openStream();

            byte[] result = zipDownloaderService.downloadBinanceHistoricalDataZip(any(), any(), any(), anyLong());

            assertArrayEquals(mockByteArray, result);
        }
    }

    @Test
    void extractCsvLinesFromZipTest() {
        // TODO: Implement tests using files, and not hardcoded
        // filename: BTCUSDT-6h-2023-09-18.csv
        //
        // file contents:
        // 1,2,3
        // 4,5,6

        List<String[]> expected = new ArrayList<>();
        expected.add(new String[]{"1", "2", "3"});
        expected.add(new String[]{"4", "5", "6"});

        TradingPair tradingPair = TradingPair.of(CurrencyEnum.BTC, CurrencyEnum.USDT);
        GranularityEnum granularity = GranularityEnum.SIX_HOURS;
        long startDate = 1694995200000L;

        String[] zipFileHexStringArray = {
                "504b0304140000000000a69a3557b4c19fdb0c0000000c00000019000000",
                "425443555344542d36682d323032332d30392d31382e637376312c322c33",
                "0a342c352c360a504b01023f03140000000000a69a3557b4c19fdb0c0000",
                "000c000000190000000000000000000000a4810000000042544355534454",
                "2d36682d323032332d30392d31382e637376504b05060000000001000100",
                "47000000430000000000"
        };
        String zipFileHexString = String.join("", zipFileHexStringArray);
        byte[] zipFileByteArray = HexFormat.of().parseHex(zipFileHexString);

        List<String[]> resultList = zipDownloaderService.extractCsvLinesFromZip(zipFileByteArray, tradingPair, granularity, startDate);

        assertEquals(expected.size(), resultList.size());
        for (int i = 0; i < expected.size(); ++i) {
            String[] expectedArr = expected.get(i);
            String[] actualArr = resultList.get(i);

            assertArrayEquals(expectedArr, actualArr);
        }
    }

    @Test
    void extractCsvLinesFromZipTest_zipWithNoEntries() {
        TradingPair tradingPair = TradingPair.of(CurrencyEnum.BTC, CurrencyEnum.USDT);
        GranularityEnum granularity = GranularityEnum.SIX_HOURS;
        long startDate = 1694995200000L;

        // zip file with no entries
        String zipFileHexString = "504b0506000000000000000000000000000000000000";
        byte[] zipFileByteArray = HexFormat.of().parseHex(zipFileHexString);

        assertThrows(IllegalStateException.class,
                () -> zipDownloaderService.extractCsvLinesFromZip(zipFileByteArray, tradingPair, granularity, startDate));
    }

    @Test
    void extractCsvLinesFromZipTest_zipDoesNotContainCorrectFile() {
        // filename: test.csv

        TradingPair tradingPair = TradingPair.of(CurrencyEnum.BTC, CurrencyEnum.USDT);
        GranularityEnum granularity = GranularityEnum.SIX_HOURS;
        long startDate = 1694995200000L;

        String[] zipFileHexStringArray = {
                "504b0304140000000000a69a3557b4c19fdb0c0000000c00000008000000",
                "746573742e637376312c322c330a342c352c360a504b01023f0314000000",
                "0000a69a3557b4c19fdb0c0000000c000000080000000000000000000000",
                "a48100000000746573742e637376504b0506000000000100010036000000",
                "320000000000"
        };
        String zipFileHexString = String.join("", zipFileHexStringArray);
        byte[] zipFileByteArray = HexFormat.of().parseHex(zipFileHexString);

        assertThrows(IllegalStateException.class,
                () -> zipDownloaderService.extractCsvLinesFromZip(zipFileByteArray, tradingPair, granularity, startDate));
    }
}
