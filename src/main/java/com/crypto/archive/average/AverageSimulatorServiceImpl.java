//package com.crypto.archive.average;
//
//import com.binance.api.client.BinanceApiClientFactory;
//import com.binance.api.client.BinanceApiRestClient;
//import com.binance.api.client.domain.market.Candlestick;
//import com.binance.api.client.domain.market.CandlestickInterval;
//import com.crypto.dto.WaveDto;
//import com.crypto.enums.WaveAction;
//import com.crypto.archive.AverageSimulatorService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicReference;
//
//@Service
//@RequiredArgsConstructor
//public class AverageSimulatorServiceImpl implements AverageSimulatorService {
//    private final BinanceApiClientFactory clientFactory;
//    private final BinanceApiRestClient restClient;
//    private final Double USDT = 1000.0;
//    AtomicReference<Double> usdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "DOGEUSDT";
//    private final Integer LIMIT = 1000;
//    private final Integer days = 7;
//    private Integer buy = 0, sellDno = 0, sellHigh = 0;
////    private Double SELL_HIGH = 100.55, BUY = 99.5, SELL_DNO = 99.2;
////    private final Integer CANDLES_LOCAL = 85;
//    private final Integer CANDLES_LOCAL = 15;
//    private Integer counter = 0;
//
//    @Override
//    public void learning(String symbol) {
////        double maxUsdt = 0, maxSellHigh = 0, maxBuy = 0, maxSellDno = 0, delta = 0.05;
////        for (double sellHigh = 100.4; sellHigh <= 100.55; sellHigh += delta) {
////            for (double buy = 99.5; buy <= 100.2; buy += delta) {
////                for (double sellDno = 99.1; sellDno <= 99.8; sellDno += delta) {
////                    if (sellDno < buy){
////                        usdt.set(USDT);
////                        amount.set(0.0);
////                        totalUsdt.set(USDT);
////                        SELL_HIGH = sellHigh;
////                        BUY = buy;
////                        SELL_DNO = sellDno;
////                        simulateResponses(symbol);
////                        System.out.printf("totalUsdt = %s,sellHigh = %s, buy = %s, sellDno = %s%n",totalUsdt, sellHigh, buy, sellDno);
////                        if (totalUsdt.get() > maxUsdt) {
////                            maxUsdt = totalUsdt.get();
////                            maxSellHigh = sellHigh;
////                            maxBuy = buy;
////                            maxSellDno = sellDno;
////                        }
////                    }
////                }
////            }
////        }
////        System.out.printf("maxUsdt = %s%n", maxUsdt);
////        System.out.printf("maxSellHigh = %s, maxBuy = %s, maxSellDno = %s", maxSellHigh, maxBuy, maxSellDno);
//        simulateResponses(symbol);
//
//    }
//
//    public double averageCalculate(WaveDto wave) {
//        double sum = 0.0;
//        for (Candlestick candlestick : wave.getCandlesticks()) {
//            sum += Double.parseDouble(candlestick.getClose());
//        }
//        return sum / wave.getCandlesticks().size();
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
//    public double rate(WaveDto wave, Candlestick response) {
//        double responseClose = Double.parseDouble(response.getClose());
//        if (wave.getCandlesticks().size() == CANDLES_LOCAL) {
//            if (response.getVolume().equals("true")) {
//                wave.getCandlesticks().remove(0);
//                wave.getCandlesticks().add(response);
//            }
//            wave.setAverage(averageCalculate(wave));
//            wave.setLastRate(responseClose / wave.getAverage() * 100);
////            System.out.println("getLastRate = " + wave.getLastRate());
//            wave.setChanged(false);
//
////            check(wave, WaveAction.SELL_HIGH);
////            check(wave, WaveAction.BUY_HIGH);
//            check(wave, WaveAction.SELL);
//            check(wave, WaveAction.BUY);
//            check(wave, WaveAction.SELL_DNO);
//
//            return wave.getChanged() ? wave.getWaveAction().getValue() : 0.0;
//        }
//        if (response.getVolume().equals("true")) {
//            double sum = wave.getAverage() * wave.getCandlesticks().size() + responseClose;
//            wave.getCandlesticks().add(response);
//            wave.setAverage(sum / wave.getCandlesticks().size());
//        }
//        return 0.0;
//    }
//
//    private void tradeDay(List<Candlestick> candlesticks, String symbol) {
//        WaveDto wave = new WaveDto();
//        wave.setSymbol(symbol);
//        for (int i = 0; i < candlesticks.size() - 1; i++) {
//            Candlestick candlestick = new Candlestick();
//            candlestick.setCloseTime(candlesticks.get(i).getCloseTime());
//            double high = Double.parseDouble(candlesticks.get(i).getHigh());
//            double low = Double.parseDouble(candlesticks.get(i).getLow());
//            double open = Double.parseDouble(candlesticks.get(i).getOpen());
//            double close = Double.parseDouble(candlesticks.get(i).getClose());
//            for (int seconds = 2; seconds <= 60; seconds += 2) {
//                if (seconds == 60) {
//                    trade(wave, candlesticks.get(i));
//                } else {
////                    if (seconds > 0 && seconds <= 10) {
////                        candlestick.setClose(String.valueOf((high - open) * seconds / 10 + open));
////                    }
////                    if (seconds > 10 && seconds <= 20) {
////                        candlestick.setClose(String.valueOf((high - open) * (20 - seconds) / 10 + open));
////                    }
////                    if (seconds > 20 && seconds <= 40) {
////                        candlestick.setClose(String.valueOf((open - close) * (40 - seconds) / 20 + close));
////                    }
////                    if (seconds > 40 && seconds <= 50) {
////                        candlestick.setClose(String.valueOf((close - low) * (seconds - 40) / 10 + low));
////                    }
////                    if (seconds > 50) {
////                        candlestick.setClose(String.valueOf((close - low) * (60 - seconds) / 10 + low));
////                    }
//                    if (seconds == 2) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.2 + open));
//                    }
//                    if (seconds == 4) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.4 + open));
//                    }
//                    if (seconds == 6) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.3 + open));
//                    }
//                    if (seconds == 8) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.5 + open));
//                    }
//                    if (seconds == 10) {
//                        candlestick.setClose(String.valueOf((high - open) * 1.0 + open));
//                    }
//                    if (seconds == 12) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.7 + open));
//                    }
//                    if (seconds == 14) {
//                        candlestick.setClose(String.valueOf((high - open) * 0.25 + open));
//                    }
//                    if (seconds == 16) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.9 + low));
//                    }
//                    if (seconds == 18) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.7 + low));
//                    }
//                    if (seconds == 20) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.8 + low));
//                    }
//                    if (seconds == 22) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.5 + low));
//                    }
//                    if (seconds == 24) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.55 + low));
//                    }
//                    if (seconds == 26) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.6 + low));
//                    }
//                    if (seconds == 28) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.71 + low));
//                    }
//                    if (seconds == 30) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.79 + low));
//                    }
//                    if (seconds == 32) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.82 + low));
//                    }
//                    if (seconds == 34) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.59 + low));
//                    }
//                    if (seconds == 36) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.48 + low));
//                    }
//                    if (seconds == 38) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.4 + low));
//                    }
//                    if (seconds == 40) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.35 + low));
//                    }
//                    if (seconds == 42) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.39 + low));
//                    }
//                    if (seconds == 44) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.3 + low));
//                    }
//                    if (seconds == 46) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.25 + low));
//                    }
//                    if (seconds == 48) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.31 + low));
//                    }
//                    if (seconds == 50) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.12 + low));
//                    }
//                    if (seconds == 52) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.0 + low));
//                    }
//                    if (seconds == 54) {
//                        candlestick.setClose(String.valueOf((open - low) * 0.2 + low));
//                    }
//                    if (seconds == 56) {
//                        candlestick.setClose(String.valueOf((open - close) * 0.5 + close));
//                    }
//                    if (seconds == 58) {
//                        candlestick.setClose(String.valueOf((open - close) * 0.8 + close));
//                    }
//                    trade(wave, candlestick);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void simulate(String symbol) {
//        List<Candlestick> candlesticks = readStatistics(symbol);
//        for (int day = 0; day < 31; day++) {
//            tradeDay(candlesticks.subList(day * LIMIT, day * LIMIT + LIMIT), symbol);
//        }
//        writeResult(candlesticks);
//    }
//
//    @Override
//    public void simulateResponses(String symbol) {
//        WaveDto wave = new WaveDto();
//        wave.setSymbol(symbol);
//        List<Candlestick> candlesticks = readResponses(symbol);
//        for (Candlestick candlestick : candlesticks) {
//            trade(wave, candlestick);
//        }
//        writeResult(candlesticks);
//    }
//
//    private void writeResult(List<Candlestick> candlesticks) {
//        System.out.println("sellHigh = " + sellHigh);
//        System.out.println("buy = " + buy);
//        System.out.println("sellDno = " + sellDno);
//        totalUsdt.set(usdt.get() + amount.get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
//        System.out.printf("total traded usdt = %s%n", totalUsdt);
//        System.out.printf("usdt passive= %s%n",
//                USDT * (Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose())
//                        / Double.parseDouble(candlesticks.get(0).getClose())));
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
//            System.out.printf("%s:time = %s, rate = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, wave.getLastRate(), close, totalUsdt, delta, usdt, SYMBOL, amount);
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
//            System.out.printf("%s:time = %s, rate = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
//                    wave.getWaveAction(), time, wave.getLastRate(), close, totalUsdt, delta * close, usdt, SYMBOL, amount);
//        }
//    }
//
//    @Override
//    public void decision(double decisionRate, WaveDto wave, Candlestick response) {
//        if (decisionRate > 0)
//            buy(decisionRate, wave, response);
//        else
//            sell(decisionRate, wave, response);
//    }
//
//    @Override
//    public List<Candlestick> fillArray() {
//        List<Candlestick> candlesticks = new ArrayList<>();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//        for (int j = 5; j <= 5; j++) {
//            for (int i = 0; i <= 31; i++) {
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
//    @Override
//    public List<Candlestick> readResponses(String symbol) {
//        ArrayList<Candlestick> candlesticks = new ArrayList<>();
//        try {
//            File file = new File("responses/" + symbol+"_4");
//            Scanner myReader = new Scanner(file);
//            while (myReader.hasNextLine()) {
//                String[] line = myReader.nextLine().split(" ");
//                Candlestick candlestick = new Candlestick();
//                candlestick.setClose(line[0]);
//                candlestick.setVolume(line[2]);
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
//    @Override
//    public void trade(WaveDto wave, Candlestick response) {
//        double decisionRate = rate(wave, response);
//        decision(decisionRate, wave, response);
//    }
//
//    @Override
//    public String logTime(long milliseconds) {
//        Date currentDate = new Date(milliseconds);
//        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
//        return df.format(currentDate);
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
//}
