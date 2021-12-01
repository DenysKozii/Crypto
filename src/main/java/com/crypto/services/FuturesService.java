package com.crypto.services;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.TickerStatistics;
import com.crypto.controllers.WaveDto;

import java.util.List;

public interface FuturesService {

    void buyLong(CandlestickEvent candlestickEvent, boolean pump, boolean dump);

    void sellShort(CandlestickEvent candlestickEvent, boolean pump, boolean dump);

    void fix(CandlestickEvent candlestickEvent);

    void writeAction(CandlestickEvent candlestickEvent, String action);

    void decision(CandlestickEvent candlestickEvent, WaveDto wave);

    void subscription(String symbol);

    void enterShorts(List<TickerStatistics> statistics);

    void enterLongs(List<TickerStatistics> statistics);
}
