package com.crypto.dto;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.enums.WaveStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.*;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TradeDto {
    private List<CandlestickEvent> candlesticks = new ArrayList<>();
    private Double oldClose;
    private Double open;
    private Double buy;
    private boolean trade;
    private WaveStatus waveStatus;

    public void insertCandlestick(CandlestickEvent candlestickEvent){
        candlesticks.add(candlestickEvent);
        if (candlesticks.size() == 6) {
            candlesticks.remove(0);
        }
        oldClose = Double.parseDouble(candlesticks.get(0).getClose());
    }
}