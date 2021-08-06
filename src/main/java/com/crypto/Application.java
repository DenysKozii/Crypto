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
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        System.out.println((1525651199999L - 1525046400000L)/60/60/60);
//        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("IA2v3GQGb4YJKdpdPZmhvlmQyyZCuP58axyri9hlMVEx0lwCwERQeWXuPmZ8k1E9", "tw2z8h8Z0jIhRMjKFz5QkAG8ulXr9HvjCWhqVBSOOcMg6GAUUaJwbToodQ4u8cX3");
//        BinanceApiRestClient client = factory.newRestClient();
//        Account account = client.getAccount();
//        System.out.println(account.getAssetBalance("XRP").getFree());

//        NewOrderResponse newOrderResponse = client.newOrder(marketBuy("XRPUSDT", "11"));
//        List<Trade> fills = newOrderResponse.getFills();
//        System.out.println(newOrderResponse.getClientOrderId());

//        BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
//        webSocketClient.onCandlestickEvent("xrpusdt", CandlestickInterval.ONE_MINUTE, System.out::println);

    }
}
