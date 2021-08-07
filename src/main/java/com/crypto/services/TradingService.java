package com.crypto.services;

import com.crypto.dto.WaveDto;

public interface TradingService {

    void buy(double decisionRate, WaveDto wave);

    void sell(double decisionRate, WaveDto wave);

    void decision(double decisionRate, WaveDto wave);

    double rate(WaveDto wave);

    void trade(WaveDto wave);

    void startTrading(String symbol);

    void writeResponse(WaveDto wave);

}
