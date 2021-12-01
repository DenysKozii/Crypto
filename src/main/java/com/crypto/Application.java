package com.crypto;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.binance.api.client.domain.market.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Objects;

@EnableScheduling
@SpringBootApplication
public class Application {
    private static final String apiKey = "IA2v3GQGb4YJKdpdPZmhvlmQyyZCuP58axyri9hlMVEx0lwCwERQeWXuPmZ8k1E9";
    private static final String secret = "tw2z8h8Z0jIhRMjKFz5QkAG8ulXr9HvjCWhqVBSOOcMg6GAUUaJwbToodQ4u8cX3";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(apiKey, secret);
//        BinanceApiRestClient client = factory.newRestClient();
//        Account account = client.getAccount();
//        System.out.println(account.getAssetBalance("DOGE").getFree());
//        NewOrderResponse newOrderResponse = client.newOrder(marketBuy("XRPUSDT", "11"));
//        List<Trade> fills = newOrderResponse.getFills();
//        System.out.println(newOrderResponse.getClientOrderId());

    }
}
