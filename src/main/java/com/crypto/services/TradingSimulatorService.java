package com.crypto.services;

import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;

import java.util.List;

public interface TradingSimulatorService {

    void simulateResponses(String symbol, List<Candlestick> candlesticks);

    void simulateDays(String symbol);

    void decision(double decisionRate, WaveDto wave, Candlestick response);

    void learning(String symbol);

    List<Candlestick> readResponses(String symbol, String filename);

    double rate(WaveDto wave, Candlestick response);

    void trade(WaveDto wave, Candlestick response);

    void buy(double decisionRate, WaveDto wave, Candlestick response);

    void sell(double decisionRate, WaveDto wave, Candlestick response);

}
