package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;

import java.util.List;

public interface SimpleTradeSimulatorService {

    void simulate(String symbol);

    void buy(double decisionRate, WaveDto wave, Candlestick response);

    void sell(double decisionRate, WaveDto wave, Candlestick response);

    void decision(double decisionRate, WaveDto wave, Candlestick response);

    List<Candlestick> fillArray();

    void writeStatistics(String symbol);

    void learning(String symbol);

    List<Candlestick> readStatistics(String symbol);

    ChannelDto getChannel(List<Candlestick> candlesticks);

    String logTime(long milliseconds);

    double rate(ChannelDto globalChannel, ChannelDto localChannel, WaveDto wave, Candlestick response);

    void trade(List<Candlestick> candlesticks, WaveDto wave, Candlestick response);

    Candlestick mapToCandlestick(String line);

}
