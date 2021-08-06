//package com.crypto.services.impl;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.domain.market.Candlestick;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.ChannelDto;
//import com.crypto.dto.WaveDto;
//import com.crypto.enums.WaveAction;
//import com.crypto.archive.TradeSimulatorService1;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import static java.lang.Math.*;
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
//public class TradeSimulatorService1Impl implements TradeSimulatorService1 {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    AtomicReference<Double> usdt = new AtomicReference<>(10000.0);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(10000.0);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "LUNAUSDT";
//    private final Integer LIMIT = 288;
//    private Integer CANDLES_IN_CHANNEL = 17;
//    private Double buyHighest = 0.1, SELL_HIGH = -0.7, SELL = -0.5, BUY_MIN = 1.0, SELL_DNO = -0.2;
//
//    @Override
//    public void learning(String symbol) {
//        double maxUsdt = 0;
//        double max_buyHighest = buyHighest, max_SELL_HIGH = SELL_HIGH, max_SELL = SELL, max_BUY_MIN = BUY_MIN, max_SELL_DNO = SELL_DNO;
//        for (double i1 = 0.1; i1 < 0.4; i1 += 0.1) {
//            for (double i2 = -1.0; i2 < -0.7; i2 += 0.1) {
//                for (double i3 = -0.5; i3 < -0.1; i3 += 0.1) {
//                    for (double i4 = 0.7; i4 < 1.0; i4 += 0.1) {
//                        for (double i5 = -0.5; i5 < -0.1; i5 += 0.1) {
//                            System.out.printf("buyHighest = %s, SELL_HIGH = %s, max_SELL = %s, BUY_MIN = %s, SELL_DNO = %s, with total usdt = %s%n",
//                                    i1, i2, i3, i4, i5, totalUsdt);
//                            System.out.printf("max_buyHighest = %s, max_SELL_HIGH = %s, max_SELL = %s, max_BUY_MIN = %s, max_SELL_DNO = %s, with total usdt = %s%n",
//                                    max_buyHighest, max_SELL_HIGH, max_SELL, max_BUY_MIN, max_SELL_DNO, maxUsdt);
//                            System.out.println("--------------------");
//                            usdt.set(10000.0);
//                            amount.set(0.0);
//                            totalUsdt.set(10000.0);
//                            buyHighest = i1;
//                            SELL_HIGH = i2;
//                            SELL = i3;
//                            BUY_MIN = i4;
//                            SELL_DNO = i5;
//                            simulate(symbol);
//                            if (totalUsdt.get() > maxUsdt) {
//                                maxUsdt = totalUsdt.get();
//                                max_buyHighest = buyHighest;
//                                max_SELL_HIGH = SELL_HIGH;
//                                max_SELL = SELL;
//                                max_BUY_MIN = BUY_MIN;
//                                max_SELL_DNO = SELL_DNO;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        System.out.println("result = ");
//        System.out.println("result = ");
//        System.out.println("result = ");
//        System.out.printf("max_buyHighest = %s, max_SELL_HIGH = %s, max_SELL = %s, max_BUY_MIN = %s, max_SELL_DNO = %s, with total usdt = %s%n",
//                max_buyHighest, max_SELL_HIGH, max_SELL, max_BUY_MIN, max_SELL_DNO, maxUsdt);
//    }
//
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
////        if (rateInChannel > 80 && rateInChannel <= 95) {
////            timer.setWaveAction(WaveAction.SELL_HIGH);
////            return SELL_HIGH;
////        }
////        if (rateInChannel > 50 && rateInChannel <= 80 && WaveAction.SELL.equals(timer.getWaveAction())) {
////            timer.setWaveAction(WaveAction.SELL);
////            return SELL;
////        }
////        if (rateInChannel > 5 && rateInChannel <= 20) {
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
//
//    @Override
//    public void buy(double decisionRate, ChannelDto channelDto) {
//        double delta = -usdt.get() * decisionRate;
//        double deltaAmount = -delta / channelDto.getLastX();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//        if (deltaAmount > 10) {
////            System.out.println("lost = " + delta);
////            System.out.println("fee = " + delta*0.00075);
//            usdt.updateAndGet(v -> v + delta + delta * 0.00075);
//            amount.updateAndGet(v -> v + deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
////            System.out.printf("BUY:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s, maxX = %s, minX = %s%n",
////                    channelDto.getTimeLastX(),
////                    channelDto.getLastX(), totalUsdt, delta, usdt, SYMBOL, amount,
////                    channelDto.getMaxX(), channelDto.getMinX());
//        }
//    }
//
//    @Override
//    public void sell(double decisionRate, ChannelDto channelDto) {
//        double deltaAmount = -decisionRate * amount.get();
//        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
//        if (deltaAmount > 10) {
////            System.out.println("get = " + deltaAmount * channelDto.getLastX());
////            System.out.println("fee = " + deltaAmount * channelDto.getLastX()*0.00075);
//            usdt.updateAndGet(v -> v + deltaAmount * channelDto.getLastX() - deltaAmount * channelDto.getLastX() * 0.00075);
//            amount.updateAndGet(v -> v - deltaAmount);
//            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * channelDto.getLastX());
////            System.out.printf("SELL:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
////                    channelDto.getTimeLastX(), channelDto.getLastX(), totalUsdt,
////                    deltaAmount * channelDto.getLastX(), usdt, SYMBOL, amount);
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
//    public List<Candlestick> fillArray() {
//        List<Candlestick> candlesticks = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        for (int j = 6; j <= 6; j++) {
//            for (int i = 1; i <= 1; i++) {
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
//    public ChannelDto channel(List<Candlestick> candlesticks, int alpha) {
//        ChannelDto channelDto = new ChannelDto();
//        double x = 0, close;
//        double maxX = -Double.MAX_VALUE, minX = Double.MAX_VALUE;
//        for (Candlestick candlestick : candlesticks) {
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
//    public void trade(List<Candlestick> candlesticks, WaveDto timer) {
//        ChannelDto channel;
//        ChannelDto finalChannel = new ChannelDto();
//        double totalDeltaMin = Double.MAX_VALUE;
//        int totalAlpha = 90;
//        for (int alpha = -90; alpha <= 90; alpha++) {
//            channel = channel(candlesticks, alpha);
//            if (channel.getDelta() < totalDeltaMin) {
//                totalDeltaMin = channel.getDelta();
//                finalChannel = channel;
//                totalAlpha = alpha;
//            }
//        }
////        System.out.println("alpha = " + totalAlpha);
//        double decisionRate = rate(finalChannel, timer);
//        decision(decisionRate, finalChannel);
//    }
//
//    @Override
//    public void simulate(String symbol) {
//        WaveDto timer = new WaveDto();
//        timer.setWaveAction(WaveAction.SELL_DNO);
//        timer.setSymbol(symbol);
//        List<Candlestick> candlesticks = readStatistics(symbol);
//        for (int i = 0; i < candlesticks.size() - CANDLES_IN_CHANNEL; i++) {
//            trade(candlesticks.subList(i, i + CANDLES_IN_CHANNEL), timer);
//        }
//        System.out.printf("total traded usdt = %s%n",
//                usdt.get() + amount.get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
//        System.out.printf(" usdt passive= %s%n",
//                10000 * (Double.valueOf(candlesticks.get(candlesticks.size() - 1).getClose()) / Double.valueOf(candlesticks.get(0).getClose())));
//    }
//
//    @Override
//    public String logTime(long milliseconds) {
//        Date currentDate = new Date(milliseconds);
//        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
//        return df.format(currentDate);
//    }
//
//    private double bijection(double number, int percentMin, int percentMax, int resMin, int resMax) {
//        int newPercent = percentMax - percentMin;
//        int newRes = resMax - resMin;
//        double newNumber = (number - percentMin) / newPercent;
//        double res = newNumber * newRes;
//        return (res + resMin) / 100;
//    }
//}
