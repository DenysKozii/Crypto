package com.crypto.dto;

import com.crypto.enums.WaveAction;
import com.crypto.enums.WaveStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WaveDto {

    private Double high = 0.0;
    private Double low = Double.MAX_VALUE;

    private String symbol;
    private Boolean pumpSignal = false;
    private Boolean dumpSignal = false;
    private WaveAction waveAction;
    private WaveStatus waveStatus;

    private Double value = 0.0;
    private Double close = 0.0;
    private Double actionPrice = 0.0;

}