package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface AverageService {

    void check(WaveDto wave, WaveAction waveAction);

    void buy(double decisionRate, WaveDto wave, CandlestickEvent response);

    void sell(double decisionRate, WaveDto wave, CandlestickEvent response);

    void decision(double decisionRate, WaveDto wave, CandlestickEvent response);

    double rate(WaveDto wave, CandlestickEvent response);

    void trade(WaveDto wave, CandlestickEvent response);

    double averageCalculate(WaveDto wave);

    void startTrading(String symbol);

    void writeAction(WaveAction waveAction,
                     LocalTime time,
                     Double price,
                     AtomicReference<Double> totalUsdt,
                     Double delta,
                     AtomicReference<Double> usdt,
                     String symbol,
                     AtomicReference<Double> amount);

    void writeResponse(WaveDto wave, CandlestickEvent response);

}
