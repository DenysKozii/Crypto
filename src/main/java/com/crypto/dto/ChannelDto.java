package com.crypto.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ChannelDto {
    private Double maxX;
    private Double minX;
    private Double lastX;
    private Double delta;
    private String timeLastX;
    private Long millisTimeLastX;

    private Integer candlesAmount;
    private List<Double> candlesClose = new ArrayList<>();

    public void update(Double response) {
        double localMaxX = -Double.MAX_VALUE, localMinX = Double.MAX_VALUE;
        for (Double close : candlesClose) {
            localMaxX = Math.max(close, localMaxX);
            localMinX = Math.min(close, localMinX);
        }
        localMaxX = Math.max(response, localMaxX);
        localMinX = Math.min(response, localMinX);

        lastX = response;
        maxX = localMaxX;
        minX = localMinX;
        delta = maxX - minX;
        candlesClose.add(response);

        if (candlesClose.size() > candlesAmount){
            candlesClose.remove(0);
        }

    }

}
