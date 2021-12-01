package com.crypto.utils;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.*;
import com.crypto.archive.TradeSimulatorService1;
import com.crypto.services.FuturesService;
import com.crypto.services.FuturesSimulatorService;
import com.crypto.services.TradingService;
import com.crypto.services.TradingSimulatorService;
import com.crypto.services.impl.angle.FuturesSimulatorServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.time.LocalTime;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class Trade {
    private final TradingSimulatorService tradingSimulatorService;
    private final TradeSimulatorService1 tradeSimulator1Service;
    private final FuturesSimulatorService futuresSimulatorService;
    private final FuturesService futuresService;
    private final TradingService tradingService;
    private final BinanceApiRestClient restClient;
    boolean LONG = true;
    private final String SAND = "SANDUSDT";
    private final String ADA = "ADAUSDT";
    private final String OGN = "OGNUSDT";
    private final String DOGE = "DOGEUSDT";
    private final String SHIB = "SHIBUSDT";
    private final String SOL = "SOLUSDT";
    private final String ETH = "ETHUSDT";
    private final String MANA = "MANAUSDT";
    private final String BTC = "BTCUSDT";
    private final String YGG = "YGGUSDT";
    private final String VGX = "VGXUSDT";
    private final String GALA = "GALAUSDT";
    private final String PORTO = "PORTOUSDT";
    private final String USDT = "USDT";
    private final String SYMBOL = "SOLUSDT";


    // 11 minutes, 10,30,60,1000 levels, 15 shoulder = 39496 DOGE 05-10
    // 85 minutes, 10,30,60,100,150 levels, 10 shoulder = 39496 DOGE 05-10
    @EventListener(ApplicationReadyEvent.class)
//    @Scheduled(fixedRate = 1000*60*60*2) // 60 minutes
    public void trade() {
        System.out.println("trade");
//        tradeSimulator1Service.writeStatistics(SYMBOL, 11, 11, 1, 27);
        futuresSimulatorService.simulateDays(SYMBOL);
    }

    private void topGainers(){
        try {
            List<TickerStatistics> topRate = restClient.getAll24HrPriceStatistics().stream()
                    .filter(o -> o.getSymbol().contains(USDT))
                    .sorted((o1, o2) -> (int) (Double.parseDouble(o2.getPriceChangePercent()) - Double.parseDouble(o1.getPriceChangePercent())))
                    .limit(10)
                    .collect(Collectors.toList());
            futuresService.enterShorts(topRate);
        } catch (Exception e) {
            System.out.println("Error while sorting");
        }
    }

    private void subscriptions(){
        Stream.of(
                SAND,
                ADA,
                OGN,
                DOGE,
                SHIB,
                SOL,
                ETH,
                MANA,
                BTC,
                YGG,
                VGX,
                GALA,
                PORTO
        ).forEach(futuresService::subscription);
    }

    private void orders(){
        LocalTime time = LocalTime.now();
        OrderBook orderBook = restClient.getOrderBook(SYMBOL, 50);
        System.out.println("Asks");
        for (int i = 0; i < orderBook.getAsks().size(); i++) {
            System.out.println(orderBook.getAsks().get(i).getPrice() + " = " + orderBook.getAsks().get(i).getQty());
        }
        System.out.println("Bids");
        for (int i = 0; i < orderBook.getBids().size(); i++) {
            System.out.println(orderBook.getBids().get(i).getPrice() + " = " + orderBook.getBids().get(i).getQty());
        }

        TickerPrice price = restClient.getPrice(SYMBOL);
        double asks = 0.0;
        for (int i = 0; i < orderBook.getAsks().size(); i++) {
            asks += Double.parseDouble(orderBook.getAsks().get(i).getQty()) * (6 - i);
        }
        double bids = 0.0;
        for (int i = 0; i < orderBook.getBids().size(); i++) {
            bids += Double.parseDouble(orderBook.getBids().get(i).getQty()) * (6 - i);
        }
        System.out.println(time + ": asks = " + asks + ", bids = " + bids + ", asks/bids = " + asks / bids);
        if (asks / bids < 0.5 && !LONG) {
            LONG = true;
            System.out.println(time + ": LONG = " + price.getPrice());
        }
        if (2 < asks / bids && LONG) {
            LONG = false;
            System.out.println(time + ": SHORT = " + price.getPrice());
        }
    }
}
