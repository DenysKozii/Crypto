package com.crypto.config;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class BinanceApiClientFactoryConfig {
    @Value("${api-key}")
    private final String apiKey = "IA2v3GQGb4YJKdpdPZmhvlmQyyZCuP58axyri9hlMVEx0lwCwERQeWXuPmZ8k1E9";
    @Value("${secret-key}")
    private final String secretKey = "tw2z8h8Z0jIhRMjKFz5QkAG8ulXr9HvjCWhqVBSOOcMg6GAUUaJwbToodQ4u8cX3";
    @Bean
    public BinanceApiClientFactory clientFactory(){
        return BinanceApiClientFactory.newInstance(apiKey, secretKey);
    }
}
