//package com.crypto.archive.simple;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.domain.market.Candlestick;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.ChannelDto;
//import com.crypto.controllers.WaveDto;
//import com.crypto.archive.SimpleTradeSimulatorService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicReference;
//
//@Service
//@RequiredArgsConstructor
//public class SimpleTradeSimulatorService1Impl implements SimpleTradeSimulatorService {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    private final Double USDT = 1000.0;
//    AtomicReference<Double> usdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "DOGEUSDT";
//    private final Integer LIMIT = 1000;
//    private Integer CANDLES_IN_GLOBAL_CHANNEL = 115;
//    private Integer CANDLES_IN_LOCAL_CHANNEL = 800;
//
//    private Integer sellCounter = 0, sellMediumCounter = 0, sellHighCounter = 0, butMinCounter = 0, sellDnoCounter = 0;
//
//    @Override
//    public void learning(String symbol) {
//        double maxUsdt = 0;
//        double av = 0;
////        for (int candles = CANDLES_IN_GLOBAL_CHANNEL; candles < CANDLES_IN_GLOBAL_CHANNEL+1; candles += 1) {
////            usdt.set(USDT);
////            amount.set(0.0);
////            totalUsdt.set(USDT);
////            CANDLES_IN_GLOBAL_CHANNEL = candles;
////            simulate(symbol);
////            if (totalUsdt.get() > maxUsdt) {
////                maxUsdt = totalUsdt.get();
////                av = candles;
////            }
////            System.out.println("candles = " + candles);
////        }
//        simulate(symbol);
//
//        System.out.println("result = " + maxUsdt + " candles = " + av);
//        System.out.println("sellDnoCounter = " + sellDnoCounter);
//        System.out.println("sellCounter = " + sellCounter);
//        System.out.println("sellMediumCounter = " + sellMediumCounter);
//        System.out.println("sellHighCounter = " + sellHighCounter);
//        System.out.println("butMinCounter = " + butMinCounter);
//    }
//
//
//    @Override
//    public double rate(ChannelDto globalChannel, ChannelDto localChannel, WaveDto wave, Candlestick response) {
//        double lastClose = Double.parseDouble(response.getClose());
//
//        double onePercentGlobal = globalChannel.getDelta() * 0.01;
//        double rateInChannelGlobal = (lastClose - globalChannel.getMinX()) / onePercentGlobal;
//        double onePercentLocal = localChannel.getDelta() * 0.01;
//        double rateInChannelLocal = (lastClose - localChannel.getMinX()) / onePercentLocal;
////        System.out.println("rateInChannelGlobal = " + rateInChannelGlobal + ", close = " + lastClose);
////        System.out.println("rateInChannelLocal = " + rateInChannelLocal + ", close = " + lastClose);
////        System.out.println("min = " + localChannel.getMinX() + " max = " + localChannel.getMaxX());
////        if (rateInChannelLocal > 250 && !wave.getWaveAction().equals(WaveAction.SELL_HIGH)) {
////            wave.setWaveAction(WaveAction.SELL_HIGH);
////            sellHighCounter++;
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannelLocal > 150 && rateInChannelLocal <= 250
////                && !wave.getWaveAction().equals(WaveAction.SELL_HIGH)
////                && !wave.getWaveAction().equals(WaveAction.SELL_MEDIUM)) {
////            wave.setWaveAction(WaveAction.SELL_MEDIUM);
////            sellMediumCounter++;
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannelLocal > 70 && rateInChannelLocal <= 150
////                && !wave.getWaveAction().equals(WaveAction.SELL)
////                && !wave.getWaveAction().equals(WaveAction.SELL_HIGH)
////                && !wave.getWaveAction().equals(WaveAction.SELL_MEDIUM)) {
////            wave.setWaveAction(WaveAction.SELL);
////            sellCounter++;
////            return wave.getWaveAction().getValue();
////        }
//
////        if (rateInChannelLocal > 0 && rateInChannelLocal < 20
////                && !wave.getWaveAction().equals(WaveAction.BUY_MIN)) {
////            wave.setWaveAction(WaveAction.BUY_MIN);
////            butMinCounter++;
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannelLocal < -10
////                && !wave.getWaveAction().equals(WaveAction.BUY)) {
////            wave.setWaveAction(WaveAction.BUY);
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannelGlobal < -10 && !wave.getWaveAction().equals(WaveAction.SELL_DNO)) {
////            wave.setWaveAction(WaveAction.SELL_DNO);
////            sellDnoCounter++;
////            return wave.getWaveAction().getValue();
////        }
//        double current = amount.get() * lastClose + usdt.get();
////        if (totalUsdt.get() + 6 < current && !WaveAction.SELL_HIGH.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.SELL_HIGH);
////            sellHighCounter++;
////            return wave.getWaveAction().getValue();
////        }
////        if (rateInChannelLocal < 40
////                && !WaveAction.SELL_DNO.equals(wave.getWaveAction())
////                && !WaveAction.BUY.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.BUY);
////            butMinCounter++;
////            return wave.getWaveAction().getValue();
////        }
////        if (totalUsdt.get() - 3 > current && !WaveAction.SELL_DNO.equals(wave.getWaveAction())) {
////            wave.setWaveAction(WaveAction.SELL_DNO);
////            sellCounter++;
////            return wave.getWaveAction().getValue();
////        }
//        return 0;
//    }
//
//    @Override
//    public void buy(double decisionRate, WaveDto wave, Candlestick response) {
//        double close = Double.parseDouble(response.getClose());
//        double delta = -usdt.get() * decisionRate;
//        double deltaAmount = -delta / close;
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//        if (deltaAmount > 10) {
//            usdt.updateAndGet(v -> v + delta + delta * 0.00075);
//            amount.updateAndGet(v -> v + deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
////            String time = logTime(response.getCloseTime());
//            String time = null;
//            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, close, totalUsdt, delta, usdt, SYMBOL, amount);
//
//        }
//    }
//
//    @Override
//    public void sell(double decisionRate, WaveDto wave, Candlestick response) {
//        double close = Double.parseDouble(response.getClose());
//        double delta = -decisionRate * amount.get();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
//        if (delta > 10) {
//            usdt.updateAndGet(v -> v + delta * close - delta * close * 0.00075);
//            amount.updateAndGet(v -> v - delta);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
////            String time = logTime(response.getCloseTime());
//            String time = null;
//            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, close, totalUsdt, delta * close, usdt, SYMBOL, amount);
//        }
//    }
//
//    @Override
//    public void decision(double decisionRate, WaveDto wave, Candlestick response) {
//        if (decisionRate > 0)
//            buy(decisionRate, wave, response);
//        else if (decisionRate < 0)
//            sell(decisionRate, wave, response);
//    }
//
//    @Override
//    public List<Candlestick> fillArray() {
//        List<Candlestick> candlesticks = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        for (int j = 6; j <= 6; j++) {
//            for (int i = 5; i <= 7; i++) {
//                if (!(j == 2 && i > 28) && !(j == 4 && i > 30)) {
//                    String startTime = String.format("%s-0%s-2021 00:00:00", i, j);
//                    String endTime = String.format("%s-0%s-2021 00:00:00", i + 1, j);
//                    try {
//                        Date dateStart = sdf.parse(startTime);
//                        Date dateEnd = sdf.parse(endTime);
//                        candlesticks.addAll(restClient.getCandlestickBars(SYMBOL, CandlestickInterval.ONE_MINUTE, LIMIT, dateStart.getTime(), dateEnd.getTime()));
//                        System.out.println(startTime);
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (ParseException | InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return candlesticks;
//    }
//
//    @Override
//    public void writeStatistics(String symbol) {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//            BufferedWriter writer = new BufferedWriter(new FileWriter("statistics/" + symbol));
//            List<Candlestick> candlesticks = fillArray();
//            for (Candlestick c : candlesticks) {
//                String stringBuilder =
//                        c.getOpenTime() + " " +
//                                c.getOpen() + " " +
//                                c.getHigh() + " " +
//                                c.getLow() + " " +
//                                c.getClose() + " " +
//                                c.getVolume() + " " +
//                                c.getCloseTime() + " " +
//                                c.getQuoteAssetVolume() + " " +
//                                c.getNumberOfTrades() + " " +
//                                c.getTakerBuyBaseAssetVolume() + " " +
//                                c.getTakerBuyQuoteAssetVolume() + " " +
//                                "\n";
//                writer.write(stringBuilder);
//            }
//            writer.close();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public List<Candlestick> readStatistics(String symbol) {
//        ArrayList<Candlestick> candlesticks = new ArrayList<>();
//        try {
//            File file = new File("statistics/" + symbol);
//            Scanner myReader = new Scanner(file);
//            while (myReader.hasNextLine()) {
//                String line = myReader.nextLine();
//                candlesticks.add(mapToCandlestick(line));
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return candlesticks;
//    }
//
//    public List<Candlestick> readResponses(String symbol) {
//        ArrayList<Candlestick> candlesticks = new ArrayList<>();
//        try {
//            File file = new File("responses/" + symbol);
//            Scanner myReader = new Scanner(file);
//            while (myReader.hasNextLine()) {
//                String line = myReader.nextLine();
//                Candlestick candlestick = new Candlestick();
//                candlestick.setClose(line);
//                candlesticks.add(candlestick);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return candlesticks;
//    }
//
//    public Candlestick mapToCandlestick(String line) {
//        String[] strings = line.split(" ");
//        Candlestick candlestick = new Candlestick();
//        candlestick.setOpenTime(Long.valueOf(strings[0]));
//        candlestick.setOpen(strings[1]);
//        candlestick.setHigh(strings[2]);
//        candlestick.setLow(strings[3]);
//        candlestick.setClose(strings[4]);
//        candlestick.setVolume(strings[5]);
//        candlestick.setCloseTime(Long.valueOf(strings[6]));
//        candlestick.setQuoteAssetVolume(strings[7]);
//        candlestick.setNumberOfTrades(Long.valueOf(strings[8]));
//        candlestick.setTakerBuyBaseAssetVolume(strings[9]);
//        candlestick.setTakerBuyQuoteAssetVolume(strings[10]);
//        return candlestick;
//    }
//
//    @Override
//    public ChannelDto getChannel(List<Candlestick> candlesticks) {
//        ChannelDto channelDto = new ChannelDto();
//        double x;
//        double maxX = -Double.MAX_VALUE, minX = Double.MAX_VALUE;
//        for (Candlestick candlestick : candlesticks) {
//            x = Double.parseDouble(candlestick.getClose());
//            maxX = Math.max(maxX, x);
//            minX = Math.min(minX, x);
//            channelDto.setLastX(x);
////            channelDto.setTimeLastX(logTime(candlestick.getCloseTime()));
////            channelDto.setMillisTimeLastX(candlestick.getCloseTime());
//        }
//        channelDto.setMinX(minX);
//        channelDto.setMaxX(maxX);
//        channelDto.setDelta(maxX - minX);
//        return channelDto;
//    }
//
//    @Override
//    public void trade(List<Candlestick> candlesticks, WaveDto wave, Candlestick response) {
//        ChannelDto globalChannel = getChannel(candlesticks);
//        ChannelDto localChannel = getChannel(candlesticks
//                .subList(candlesticks.size() - CANDLES_IN_LOCAL_CHANNEL, candlesticks.size() - 1));
//        double decisionRate = rate(globalChannel, localChannel, wave, response);
//        decision(decisionRate, wave, response);
//    }
//
//    @Override
//    public void simulate(String symbol) {
//        WaveDto wave = new WaveDto();
//        wave.setSymbol(symbol);
//        List<Candlestick> candlesticks = readResponses(symbol);
////        List<Candlestick> candlesticks = readStatistics(symbol);
////        for (int i = 0; i < candlesticks.size() - CANDLES_IN_GLOBAL_CHANNEL - 1; i++) {
////            for (int seconds = 2; seconds <= 60; seconds += 2) {
////                if (seconds == 60) {
////                    trade(candlesticks.subList(i, i + CANDLES_IN_GLOBAL_CHANNEL), wave, candlesticks.get(i + CANDLES_IN_GLOBAL_CHANNEL + 1));
////                } else {
////                    Candlestick candlestick = candlesticks.get(i + CANDLES_IN_GLOBAL_CHANNEL + 1);
////                    double high = Double.parseDouble(candlestick.getHigh());
////                    double low = Double.parseDouble(candlestick.getLow());
////                    candlestick.setClose(String.valueOf((high - low) * (62 - seconds) / 60 + low));
////                    trade(candlesticks.subList(i, i + CANDLES_IN_GLOBAL_CHANNEL), wave, candlestick);
////                }
////            }
////        }
//        for (int i = 0; i < candlesticks.size() - CANDLES_IN_LOCAL_CHANNEL - 1; i++) {
//            trade(candlesticks.subList(i, i + CANDLES_IN_LOCAL_CHANNEL), wave, candlesticks.get(i + CANDLES_IN_LOCAL_CHANNEL + 1));
//        }
//
//        System.out.printf("total traded usdt = %s%n",
//                usdt.get() + amount.get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
//        System.out.printf("usdt passive= %s%n",
//                USDT * (Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose())
//                        / Double.parseDouble(candlesticks.get(0).getClose())));
//    }
//
//    @Override
//    public String logTime(long milliseconds) {
//        Date currentDate = new Date(milliseconds);
//        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
//        return df.format(currentDate);
//    }
//}
