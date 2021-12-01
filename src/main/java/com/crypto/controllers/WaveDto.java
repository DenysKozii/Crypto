package com.crypto.controllers;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.enums.WaveAction;
import com.crypto.enums.WaveStatus;
import com.crypto.indicators.EMA;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WaveDto {
    private String symbol;
    private WaveAction action;
    private WaveStatus status;

    private Double value = 0.0;
    private Double close = 0.0;
    private Double prevClose = 0.0;

    private CandlestickEvent candlestickEvent;

    private List<EMA> emas = new ArrayList<>();
}