package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;

import java.util.List;

public interface FuturesSimulatorService {

    void simulateResponses(String symbol, List<CandlestickEvent> candlesticks);

    void simulateDays(String symbol);

    List<CandlestickEvent> readResponses(String symbol);

}
