package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface SimpleTradeService1 {

//    void changeWave(WaveDto wave, ChannelDto channel, List<CandlestickEvent> candlesticks);

    void buy(double decisionRate, WaveDto wave, CandlestickEvent response);

    void sell(double decisionRate, WaveDto wave, CandlestickEvent response);

    void decision(double decisionRate, WaveDto wave, CandlestickEvent response);

    ChannelDto channel(List<CandlestickEvent> candlesticks);

//    ChannelDto channelByWave(List<CandlestickEvent> candlesticks);

    double rate(ChannelDto channelDto, WaveDto timer, CandlestickEvent response);

    void trade(List<CandlestickEvent> candlesticks, WaveDto timer, CandlestickEvent response);

    void startTrading(String symbol);

    void writeAction(WaveAction waveAction,
                     LocalTime time,
                     Double price,
                     AtomicReference<Double> totalUsdt,
                     Double delta,
                     AtomicReference<Double> usdt,
                     String symbol,
                     AtomicReference<Double> amount);
}
