package com.crypto.archive;

import com.binance.api.client.domain.market.Candlestick;

import java.util.List;

public interface TradeSimulatorService1 {


    List<Candlestick> fillArray(int monthStart, int monthEnd,int dayStart, int dayEnd);

    void writeStatistics(String symbol, int monthStart, int monthEnd,int dayStart, int dayEnd);

    String logTime(long milliseconds);

}
