package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.List;

public interface TradingSimulatorService {

    void simulateResponses(String symbol, List<CandlestickEvent> candlesticks);

    void simulateDays(String symbol);

    void learning(String symbol);

    List<CandlestickEvent> readResponses(String symbol, String filename);

}
