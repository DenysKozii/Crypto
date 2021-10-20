package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;

import java.util.List;

public interface TradeSimulatorService1 {

//    void simulate(String symbol);


    List<Candlestick> fillArray();

    void writeStatistics(String symbol);

//    List<Candlestick> readStatistics(String symbol);

    String logTime(long milliseconds);

//    Candlestick mapToCandlestick(String line);

}
