package com.crypto.archive;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.dto.ChannelDto;
import com.crypto.dto.WaveDto;

import java.util.List;

public interface TradeService2 {

    void buy(double decisionRate, ChannelDto channelDto);

    void sell(double decisionRate, ChannelDto channelDto);

    void decision(double decisionRate, ChannelDto channelDto);

    ChannelDto channel(List<CandlestickEvent> candlesticks, int alpha);

    String logTime(long milliseconds);

    double rate(ChannelDto channelDto, WaveDto timer);

    void trade(List<CandlestickEvent> candlesticks, WaveDto timer);

    void startTrading(String symbol);

}
