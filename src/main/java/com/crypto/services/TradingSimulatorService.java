package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.List;

public interface TradingSimulatorService {

    void simulateResponses(String symbol, List<CandlestickEvent> candlesticks);

    void simulateDays(String symbol);

//    void learning(String symbol);
    void learningDelta(String symbol);
    void learningSellPercent(String symbol);
    void learningBuyPercent(String symbol);

    List<CandlestickEvent> readResponses(String symbol, String filename);

}
