package com.mihmin98.cryptotrackerbackend.controller;

import com.mihmin98.cryptotrackerbackend.binance.enums.GranularityEnum;
import com.mihmin98.cryptotrackerbackend.model.TradingPair;
import com.mihmin98.cryptotrackerbackend.model.TradingPairPrice;
import com.mihmin98.cryptotrackerbackend.service.TradingPairPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/price")
public class TradingPairPriceHistoryController {

    @Autowired
    private TradingPairPriceService tradingPairPriceService;

    /*
    cred ca o sa am doar un getter
     */

    @GetMapping
    public ResponseEntity<?> getPriceHistory(
            @RequestParam(name = "tradingPair") String tradingPairStr,
            @RequestParam(name = "granularity") String granularityStr,
            @RequestParam(name = "startTime") String startTimeUTC,
            @RequestParam(name = "endTime") String endTimeUTC) {

        GranularityEnum granularity = GranularityEnum.fromValue(granularityStr);
        TradingPair tradingPair = TradingPair.of(tradingPairStr);

        List<TradingPairPrice> result = tradingPairPriceService.getPriceHistory(tradingPair, granularity, startTimeUTC, endTimeUTC);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
