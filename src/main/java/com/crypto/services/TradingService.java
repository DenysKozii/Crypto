package com.crypto.services;

import com.crypto.controllers.WaveDto;

public interface TradingService {

    void buy(double decisionRate, WaveDto wave, boolean simulate);

    void sell(double decisionRate, WaveDto wave, boolean simulate);

    void writeAction(WaveDto wave, boolean simulate);

    void decision(double decisionRate, WaveDto wave, boolean simulate);

    double rate(WaveDto wave);

    void trade(WaveDto wave, boolean simulate);

    void startTrading(String symbol);

    void writeResponse(WaveDto wave);

}
