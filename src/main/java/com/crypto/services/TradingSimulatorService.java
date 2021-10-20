package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.List;

public interface TradingSimulatorService {

    void simulateResponses(String symbol, List<CandlestickEvent> candlesticks, boolean simulate);

    void simulateDays(String symbol, boolean simulate);

    void learning(String symbol, boolean simulate);

    List<CandlestickEvent> readResponses(String symbol, String filename);

}
