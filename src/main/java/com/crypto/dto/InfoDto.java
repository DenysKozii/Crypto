package com.crypto.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InfoDto {
    private String action;
    private String time;
    private String date;
    private String price;
    private String usdt;
    private String usdtPassive;
    private String status;
}