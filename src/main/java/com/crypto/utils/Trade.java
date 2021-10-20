package com.crypto.utils;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.crypto.archive.TradeSimulatorService1;
import com.crypto.services.TradingService;
import com.crypto.services.TradingSimulatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Trade {
    private final BinanceApiWebSocketClient webSocketClient;
    private final TradingSimulatorService tradinSimulatorService;
    private final TradeSimulatorService1 tradeSimulator1Service;
    private final TradingService tradingService;
    private final BinanceApiRestClient restClient;

//    private final String SYMBOL = "XTZUSDT";
//    private final String SYMBOL = "DOGEUSDT";
    private final String SYMBOL = "XRPUSDT";

    @EventListener(ApplicationReadyEvent.class)
    public void firstInit() {
//        Account account = restClient.getAccount();
        trade();
    }

    private void trade() {
//        tradeSimulator1Service.writeStatistics(SYMBOL);
        tradinSimulatorService.learning(SYMBOL, true);
//        tradingService.startTrading(SYMBOL);
    }
}
