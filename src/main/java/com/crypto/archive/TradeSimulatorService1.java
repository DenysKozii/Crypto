package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;

import java.util.List;

public interface TradeSimulatorService1 {

    void simulate(String symbol);

    void buy(double decisionRate, ChannelDto channelDto);

    void sell(double decisionRate, ChannelDto channelDto);

    void decision(double decisionRate, ChannelDto channelDto);

    List<Candlestick> fillArray();

    void writeStatistics(String symbol);

    void learning(String symbol);

    List<Candlestick> readStatistics(String symbol);

    ChannelDto channel(List<Candlestick> candlesticks, int alpha);

    String logTime(long milliseconds);

    double rate(ChannelDto channelDto, WaveDto timer);

    void trade(List<Candlestick> candlesticks, WaveDto wave);

    public Candlestick mapToCandlestick(String line);

}
