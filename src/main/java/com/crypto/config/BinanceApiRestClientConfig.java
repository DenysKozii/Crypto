package com.crypto.config;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class BinanceApiRestClientConfig {
    private final BinanceApiClientFactory factory;

    @Bean
    public BinanceApiRestClient restClient() {
        return factory.newRestClient();
    }
}
