//package com.crypto.services.impl;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.BinanceApiWebSocketClient;
//import com.binance.api.client.domain.event.CandlestickEvent;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.ChannelDto;
//import com.crypto.dto.WaveDto;
//import com.crypto.enums.WaveAction;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicReference;
//
//import com.crypto.archive.TradeService2;
//
//import static java.lang.Math.*;
//
//@Service
//@RequiredArgsConstructor
//public class TradeServiceImpl implements TradeService2 {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    private final BinanceApiWebSocketClient webSocketClient;
//    AtomicReference<Double> usdt = new AtomicReference<>(1000.0);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(10000.0);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "LUNAUSDT";
//    private final Integer CANDLES_IN_CHANNEL = 17;
//    private Double buyHighest = 0.1, SELL_HIGH = -0.7, SELL = -0.4, BUY_MIN = 1.0, SELL_DNO = -0.2;
//
//
//    @Override
//    public void buy(double decisionRate, ChannelDto channelDto) {
//        double delta = -usdt.get() * decisionRate;
//        double deltaAmount = -delta / channelDto.getLastX();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//        if (deltaAmount > 10) {
//            usdt.updateAndGet(v -> v + delta + delta * 0.00075);
//            amount.updateAndGet(v -> v + deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//            System.out.printf("BUY:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s, maxX = %s, minX = %s%n",
//                    channelDto.getTimeLastX(),
//                    channelDto.getLastX(), usdt.get() + amount.get() * channelDto.getLastX(), delta, usdt, SYMBOL, amount,
//                    channelDto.getMaxX(), channelDto.getMinX());
//        }
//    }
//
//    @Override
//    public void sell(double decisionRate, ChannelDto channelDto) {
//        double deltaAmount = -decisionRate * amount.get();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//        if (deltaAmount > 10) {
//            usdt.updateAndGet(v -> v + deltaAmount * channelDto.getLastX() - deltaAmount * channelDto.getLastX() * 0.00075);
//            amount.updateAndGet(v -> v - deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//            System.out.printf("SELL:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    channelDto.getTimeLastX(), channelDto.getLastX(), usdt.get() + amount.get() * channelDto.getLastX(),
//                    deltaAmount * channelDto.getLastX(), usdt, SYMBOL, amount);
//        }
//    }
//
//    @Override
//    public void decision(double decisionRate, ChannelDto channelDto) {
//        if (decisionRate > 0)
//            buy(decisionRate, channelDto);
//        else
//            sell(decisionRate, channelDto);
//    }
//
//    @Override
//    public ChannelDto channel(List<CandlestickEvent> candlesticks, int alpha) {
//        ChannelDto channelDto = new ChannelDto();
//        double x = 0, close;
//        double maxX = -Double.MAX_VALUE, minX = Double.MAX_VALUE;
//        for (CandlestickEvent candlestick : candlesticks) {
//            close = Double.parseDouble(candlestick.getClose());
//            x = close * cos(alpha) - candlesticks.indexOf(candlestick) * sin(alpha);
//            if (maxX < x) {
//                maxX = x;
//                channelDto.setMaxX(close);
//            }
//            if (minX > x) {
//                minX = x;
//                channelDto.setMinX(close);
//            }
//            maxX = max(maxX, x);
//            minX = min(minX, x);
//            channelDto.setLastX(close);
//            channelDto.setTimeLastX(logTime(candlestick.getCloseTime()));
//            channelDto.setMillisTimeLastX(candlestick.getCloseTime());
//        }
//        channelDto.setDelta(maxX - minX);
//        return channelDto;
//    }
//
//    @Override
//    public double rate(ChannelDto channelDto, WaveDto timer) {
//        double onePercent = channelDto.getDelta() * 0.01;
//        double lastClose = channelDto.getLastX();
//        double rateInChannel = (lastClose - channelDto.getMinX()) / onePercent;
////        System.out.println("rateInChannel = " + rateInChannel);
////        if (rateInChannel > 100) {
////            timer.setWaveAction(WaveAction.BUY_HIGHEST);
////            return buyHighest;
////        }
////        if (rateInChannel > 80 && rateInChannel <= 100) {
//////        if (rateInChannel > 80 && rateInChannel <= 95 && !Level.SELL_HIGH.equals(timer.getTradeLevel())) {
////            timer.setWaveAction(WaveAction.SELL_HIGH);
////            return SELL_HIGH;
////        }
////        if (rateInChannel > 50 && rateInChannel <= 80) {
//////        if (rateInChannel > 50 && rateInChannel <= 80 && !Level.SELL.equals(timer.getTradeLevel())) {
////            timer.setWaveAction(WaveAction.SELL);
////            return SELL;
////        }
////        if (rateInChannel > 0 && rateInChannel <= 20) {
//////        if (rateInChannel > 5 && rateInChannel <= 20 && !Level.BUY_MIN.equals(timer.getTradeLevel())) {
////            timer.setWaveAction(WaveAction.BUY_MIN);
////            return BUY_MIN;
////        }
////        if (rateInChannel <= 0) {
////            timer.setWaveAction(WaveAction.SELL_DNO);
////            return SELL_DNO;
////        }
//        return 0;
//    }
//
//    @Override
//    public void trade(List<CandlestickEvent> candlesticks, WaveDto timer) {
//        ChannelDto channel;
//        ChannelDto finalChannel = new ChannelDto();
//        double totalDeltaMin = Double.MAX_VALUE;
//        for (int alpha = -90; alpha <= 90; alpha++) {
//            channel = channel(candlesticks, alpha);
//            if (channel.getDelta() < totalDeltaMin) {
//                totalDeltaMin = channel.getDelta();
//                finalChannel = channel;
//            }
//        }
//        double decisionRate = rate(finalChannel, timer);
////        System.out.println("decisionRate = " + decisionRate);
//        decision(decisionRate, finalChannel);
//    }
//
//    @Override
//    public void startTrading(String symbol) {
//        WaveDto timer = new WaveDto();
//        timer.setWaveAction(WaveAction.SELL_DNO);
//        timer.setSymbol(symbol);
//        ArrayList<CandlestickEvent> candlesticks = new ArrayList<>();
//        webSocketClient.onCandlestickEvent(symbol.toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {
//            if (Objects.equals(response.getSymbol(), symbol)) {
//                if (response.getBarFinal()) {
//                    System.out.println("close = " + response.getClose());
//                    System.out.println("total usdt = " + usdt.get() + amount.get() * Double.valueOf(response.getClose()));
//                    if (candlesticks.size() > CANDLES_IN_CHANNEL) {
//                        candlesticks.remove(0);
//                        candlesticks.add(response);
//                        trade(candlesticks, timer);
//                    }
//                }
////                if (candlesticks.size() == CANDLES_IN_CHANNEL) {
////                    candlesticks.add(response);
////                    candlesticks.remove(response);
////                }
//            }
//        });
//    }
//
//    @Override
//    public String logTime(long milliseconds) {
//        Date currentDate = new Date(milliseconds);
//        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
//        return df.format(currentDate);
//    }
//}
