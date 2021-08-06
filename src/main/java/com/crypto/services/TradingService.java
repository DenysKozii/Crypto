package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicReference;

public interface TradingService {

    void buy(double decisionRate, WaveDto wave, CandlestickEvent response);

    void sell(double decisionRate, WaveDto wave, CandlestickEvent response);

    void decision(double decisionRate, WaveDto wave, CandlestickEvent response);

    double rate(WaveDto wave, CandlestickEvent response);

    void trade(WaveDto wave, CandlestickEvent response);

    void startTrading(String symbol);

    void writeAction(WaveAction waveAction,
                     LocalTime time,
                     Double price,
                     AtomicReference<Double> totalUsdt,
                     Double delta,
                     AtomicReference<Double> usdt,
                     String symbol,
                     AtomicReference<Double> amount);

    void writeResponse(WaveDto wave);

}
