package com.crypto.archive;

import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;

import java.util.List;

public interface AverageSimulatorService {

    void check(WaveDto wave, WaveAction waveAction);

    void simulate(String symbol);

    void simulateResponses(String symbol);

    void buy(double decisionRate, WaveDto wave, Candlestick response);

    void sell(double decisionRate, WaveDto wave, Candlestick response);

    void decision(double decisionRate, WaveDto wave, Candlestick response);

    List<Candlestick> fillArray();

    void writeStatistics(String symbol);

    void learning(String symbol);

    List<Candlestick> readStatistics(String symbol);

    List<Candlestick> readResponses(String symbol);

    String logTime(long milliseconds);

    double rate(WaveDto wave, Candlestick response);

    void trade(WaveDto wave, Candlestick response);

    Candlestick mapToCandlestick(String line);

}
