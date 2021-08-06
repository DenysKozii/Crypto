//package com.crypto.archive.average;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.BinanceApiWebSocketClient;
//import com.binance.api.client.domain.event.CandlestickEvent;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.WaveDto;
//import com.crypto.enums.WaveAction;
//import com.crypto.archive.AverageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.LocalTime;
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicReference;
//
//@Service
//@RequiredArgsConstructor
//public class AverageServiceImpl implements AverageService {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    private final BinanceApiWebSocketClient webSocketClient;
//    AtomicReference<Double> usdt = new AtomicReference<>(1000.0);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(1000.0);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "DOGEUSDT";
//    //    private Double sellHighCoef = 100.38, buyCoef = 100.14, sellDnoCoef = 99.7;
////    private Double SELL_HIGH = 100.82, BUY = 99.5, SELL_DNO = 99.22;
//    private final Integer CANDLES = 7;
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
//    public void writeResponse(WaveDto wave, CandlestickEvent response) {
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("responses/" + wave.getSymbol() + "_4", true));
//            LocalTime time = LocalTime.now();
//            String row = response.getClose() + " " + time + " " + response.getBarFinal() + "\n";
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
//    @Override
//    public void decision(double decisionRate, WaveDto wave, CandlestickEvent response) {
//        if (decisionRate > 0)
//            buy(decisionRate, wave, response);
//        else
//            sell(decisionRate, wave, response);
//    }
//
//    @Override
//    public double averageCalculate(WaveDto wave) {
//        double sum = 0.0;
//        for (CandlestickEvent candlestickEvent : wave.getCandlestickEvents()) {
//            sum += Double.parseDouble(candlestickEvent.getClose());
//        }
//        return sum / wave.getCandlestickEvents().size();
//    }
//
//    @Override
//    public void check(WaveDto wave, WaveAction waveAction) {
//        if (wave.getLastRate() > waveAction.getLow()
//                && wave.getLastRate() < waveAction.getHigh()
//                && !waveAction.equals(wave.getWaveAction())
//                && !(WaveAction.BUY.equals(waveAction) && (WaveAction.SELL_DNO.equals(wave.getWaveAction())))
//        ) {
//            wave.setWaveAction(waveAction);
//            wave.setChanged(true);
//        }
//    }
//
//    @Override
//    public double rate(WaveDto wave, CandlestickEvent response) {
//        double responseClose = Double.parseDouble(response.getClose());
//        if (wave.getCandlestickEvents().size() == CANDLES) {
//            if (response.getBarFinal()) {
//                wave.getCandlestickEvents().remove(0);
//                wave.getCandlestickEvents().add(response);
//            }
//
//            wave.setAverage(averageCalculate(wave));
//            wave.setLastRate(responseClose / wave.getAverage() * 100);
//            wave.setChanged(false);
//
//            LocalTime time = LocalTime.now();
//            System.out.println(time + " = " + wave.getLastRate());
//
//            check(wave, WaveAction.SELL);
//            check(wave, WaveAction.BUY);
//            check(wave, WaveAction.SELL_DNO);
//
//            return wave.getChanged() ? wave.getWaveAction().getValue() : 0.0;
//        }
//        if (response.getBarFinal()) {
//            wave.getCandlestickEvents().add(response);
//        }
//        return 0.0;
//    }
//
//    @Override
//    public void trade(WaveDto wave, CandlestickEvent response) {
//        double decisionRate = rate(wave, response);
//        decision(decisionRate, wave, response);
//    }
//
//    @Override
//    public void startTrading(String symbol) {
//        WaveDto wave = new WaveDto();
//        wave.setSymbol(symbol);
//        webSocketClient.onCandlestickEvent(symbol.toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {
//            if (Objects.equals(response.getSymbol(), symbol)) {
//                if (response.getBarFinal()) {
//                    System.out.println("total usdt = " + (usdt.get() + amount.get() * Double.parseDouble(response.getClose())));
//                }
//                trade(wave, response);
//                writeResponse(wave, response);
//            }
//        });
//    }
//}
