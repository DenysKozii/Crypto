//package com.crypto.archive.simple;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.BinanceApiWebSocketClient;
//import com.binance.api.client.domain.event.CandlestickEvent;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.ChannelDto;
//import com.crypto.dto.WaveDto;
//import com.crypto.enums.WaveAction;
//import com.crypto.archive.SimpleTradeService1;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class SimpleTradeService1Impl implements SimpleTradeService1 {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    private final BinanceApiWebSocketClient webSocketClient;
//    AtomicReference<Double> usdt = new AtomicReference<>(1000.0);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(1000.0);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "DOGEUSDT";
//    private final Integer CANDLES_IN_CHANNEL = 45;
//
//
//    @Override
//    public void writeAction(WaveAction waveAction, LocalTime time, Double price, AtomicReference<Double> totalUsdt, Double delta, AtomicReference<Double> usdt, String symbol, AtomicReference<Double> amount) {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("trading/" + symbol, true));
//            String row = String.format("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    waveAction, time, price, totalUsdt, delta, usdt, SYMBOL, amount);
//            writer.write(row);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void buy(double decisionRate, WaveDto wave, CandlestickEvent response) {
//        double close = Double.parseDouble(response.getClose());
//        double delta = -usdt.get() * decisionRate;
//        double deltaAmount = -delta / close;
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//        if (deltaAmount > 10) {
//            usdt.updateAndGet(v -> v + delta + delta * 0.00075);
//            amount.updateAndGet(v -> v + deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//            LocalTime time = LocalTime.now();
//            writeAction(wave.getWaveAction(), time, close, totalUsdt, delta, usdt, SYMBOL, amount);
//            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, close, totalUsdt, delta, usdt, SYMBOL, amount);
//        }
//    }
//
//    @Override
//    public void sell(double decisionRate, WaveDto wave, CandlestickEvent response) {
//        double close = Double.parseDouble(response.getClose());
//        double delta = -decisionRate * amount.get();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//        if (delta > 10) {
//            usdt.updateAndGet(v -> v + delta * close - delta * close * 0.00075);
//            amount.updateAndGet(v -> v - delta);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//            LocalTime time = LocalTime.now();
//            writeAction(wave.getWaveAction(), time, close, totalUsdt, delta * close, usdt, SYMBOL, amount);
//            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, close, totalUsdt, delta * close, usdt, SYMBOL, amount);
//        }
//    }
//
//
//    @Override
//    public void decision(double decisionRate, WaveDto wave, CandlestickEvent response) {
//        if (decisionRate > 0)
//            buy(decisionRate, wave, response);
//        else
//            sell(decisionRate, wave, response);
//    }
//
//    @Override
//    public ChannelDto channel(List<CandlestickEvent> candlesticks) {
//        ChannelDto channelDto = new ChannelDto();
//        double x;
//        double maxX = -Double.MAX_VALUE, minX = Double.MAX_VALUE;
//        for (CandlestickEvent candlestickEvent : candlesticks) {
//            x = Double.parseDouble(candlestickEvent.getClose());
//            maxX = Math.max(maxX, x);
//            minX = Math.min(minX, x);
//            channelDto.setLastX(x);
//            channelDto.setMillisTimeLastX(candlestickEvent.getCloseTime());
//        }
//        channelDto.setMinX(minX);
//        channelDto.setMaxX(maxX);
//        channelDto.setDelta(maxX - minX);
//        return channelDto;
//    }
//
//    @Override
//    public double rate(ChannelDto channelDto, WaveDto wave, CandlestickEvent response) {
//        double onePercent = channelDto.getDelta() * 0.01;
//        double lastClose = Double.parseDouble(response.getClose());
//        double rateInChannel = (lastClose - channelDto.getMinX()) / onePercent;
//        LocalTime time = LocalTime.now();
//        System.out.println("time = " + time + ", rateInChannel = " + rateInChannel + ", lastClose = " + lastClose);
//////        if (rateInChannel > 105 && !WaveAction.BUY_HIGHEST.equals(wave.getWaveAction())){
//////            wave.setWaveAction(WaveAction.BUY_HIGHEST);
//////            return wave.getWaveAction().getValue();
//////        }
////        if (rateInChannel > 70 && rateInChannel <= 100 && !WaveAction.SELL_HIGH.equals(wave.getWaveAction())){
////            wave.setWaveAction(WaveAction.SELL_HIGH);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel > 40 && rateInChannel <= 55 && !WaveAction.SELL.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.SELL);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel > 12 && rateInChannel < 25
////                && !WaveAction.BUY_MIN.equals(wave.getWaveAction())
////                && !WaveAction.BUY.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.BUY);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel > 0 && rateInChannel < 10
////                && !WaveAction.SELL_DNO.equals(wave.getWaveAction())
////                && !WaveAction.BUY_MIN.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.BUY_MIN);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel < 0) {
////            wave.setWaveAction(WaveAction.SELL_DNO);
////            return wave.getWaveAction().getValue();
////        }
////        return 0;
////        if (rateInChannel > 70 && rateInChannel <= 100 && !wave.getWaveAction().equals(WaveAction.SELL_HIGH)) {
////            wave.setWaveAction(WaveAction.SELL_HIGH);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel > 45 && rateInChannel <= 55 && !wave.getWaveAction().equals(WaveAction.SELL)) {
////            wave.setWaveAction(WaveAction.SELL);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel > 0 && rateInChannel < 10
////                && !wave.getWaveAction().equals(WaveAction.SELL_DNO)
////                && !wave.getWaveAction().equals(WaveAction.BUY_MIN)) {
////            wave.setWaveAction(WaveAction.BUY_MIN);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannel < -15 && !wave.getWaveAction().equals(WaveAction.SELL_DNO)) {
////            wave.setWaveAction(WaveAction.SELL_DNO);
////            return wave.getWaveAction().getValue();
////        }
//        return 0;
//    }
//
//    @Override
//    public void trade(List<CandlestickEvent> candlesticks, WaveDto wave, CandlestickEvent response) {
//        ChannelDto channel = channel(candlesticks);
//        double decisionRate = rate(channel, wave, response);
//        decision(decisionRate, wave, response);
//    }
//
//    @Override
//    public void startTrading(String symbol) {
////        WaveDto wave = new WaveDto();
////        wave.setWaveAction(WaveAction.SELL_DNO);
////        wave.setSymbol(symbol);
////        ArrayList<CandlestickEvent> candlesticks = new ArrayList<>();
//        CandlestickEvent prev1 = new CandlestickEvent();
//        prev1.setClose("0.0");
////        CandlestickEvent prev2 = new CandlestickEvent();
//        webSocketClient.onCandlestickEvent(symbol.toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {
//            if (Objects.equals(response.getSymbol(), symbol)) {
////                if (response.getBarFinal()) {
////                    System.out.println("total usdt = " + (usdt.get() + amount.get() * Double.parseDouble(response.getClose())));
////                    candlesticks.add(response);
////                    if (candlesticks.size() > CANDLES_IN_CHANNEL) {
////                        candlesticks.remove(0);
////                        trade(candlesticks, wave, response);
////                    }
////                } else {
////                    if (candlesticks.size() == CANDLES_IN_CHANNEL) {
////                        trade(candlesticks, wave, response);
////                    }
////                }
//                double percent = Double.parseDouble(prev1.getClose()) * 0.01;
//                LocalTime time = LocalTime.now();
//                System.out.print("time = " + time + " = ");
//                System.out.println(Double.parseDouble(response.getClose()) / percent - 100);
//                prev1.setClose(response.getClose());
//            }
//        });
//    }
//
//    private void printCandlesticks(List<CandlestickEvent> candlesticks) {
//        List<String> closes = candlesticks.stream().map(CandlestickEvent::getClose).collect(Collectors.toList());
//        System.out.println(closes);
//    }
//}
