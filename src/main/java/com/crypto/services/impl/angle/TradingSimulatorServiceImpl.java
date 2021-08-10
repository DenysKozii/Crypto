package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;
import com.crypto.services.TradingSimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class TradingSimulatorServiceImpl implements TradingSimulatorService {
    private final BinanceApiClientFactory clientFactory;
    private final BinanceApiRestClient restClient;
    private final TradingServiceImpl tradingService;
//    private final Double USDT = 1000.0;
//    AtomicReference<Double> usdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> totalUsdt = new AtomicReference<>(USDT);
//    AtomicReference<Double> amount = new AtomicReference<>(0.0);
//    private final String SYMBOL = "DOGEUSDT";

/*
    @Override
    public void learning(String symbol) {
        double maxUsdt = 0;
        double maxsellPercent = 0, maxbuyPercent = 0, maxdelta = 0, maxLastTradeDelta = 0;
//        for (DNO_PERCENT = -30; DNO_PERCENT <= 80; DNO_PERCENT += 1) {
//        for (Integer buyPercent = 0; buyPercent <= 20; buyPercent += 1) {
        for (Integer sellPercent = 80; sellPercent <= 95; sellPercent += 1) {
            //tradingService.setSELL_PERCENT(sellPercent);

            tradingService.getUSDT().updateAndGet(v -> tradingService.getSTART_USDT());
            tradingService.getAmount().updateAndGet(v -> 0.0);
            tradingService.getTotalUsdt().updateAndGet(v -> tradingService.getSTART_USDT());
            simulateDays(symbol);
            System.out.printf("totalUsdt = %s", tradingService.getTotalUsdt().get());
            if (tradingService.getTotalUsdt().get() > maxUsdt) {
                maxUsdt = tradingService.getTotalUsdt().get();
                maxsellPercent = tradingService.getSELL_PERCENT();
                maxbuyPercent = tradingService.getBUY_PERCENT();
                maxdelta = tradingService.getDELTA();
            }
        }
        System.out.printf("maxUsdt = %s%n", maxUsdt);
        System.out.printf("sellPercent = %s%n", maxsellPercent);
        System.out.printf("buyPercent = %s%n", maxbuyPercent);
        System.out.printf("maxdelta = %s%n", maxdelta);
        System.out.printf("maxLastTradeDelta = %s%n", maxLastTradeDelta);
//        simulateDays(symbol);
    }
*/
    @Override
    public void learningDelta(String symbol) {
        double maxUsdt = 0;
        double maxsellPercent = 0, maxbuyPercent = 0, maxdelta = 0, maxLastTradeDelta = 0;
        for (double delta = 0.002; delta <= 0.006; delta += 0.0001) {
            tradingService.setDELTA(delta);

            tradingService.getUSDT().updateAndGet(v -> tradingService.getSTART_USDT());
            tradingService.getAmount().updateAndGet(v -> 0.0);
            tradingService.getTotalUsdt().updateAndGet(v -> tradingService.getSTART_USDT());
            simulateDays(symbol);
            System.out.printf("totalUsdt = %s", tradingService.getTotalUsdt().get());
            if (tradingService.getTotalUsdt().get() > maxUsdt) {
                maxUsdt = tradingService.getTotalUsdt().get();
                maxsellPercent = tradingService.getSELL_PERCENT();
                maxbuyPercent = tradingService.getBUY_PERCENT();
                maxdelta = delta;
            }
        }
        System.out.printf("maxUsdt = %s%n", maxUsdt);
        System.out.printf("sellPercent = %s%n", maxsellPercent);
        System.out.printf("buyPercent = %s%n", maxbuyPercent);
        System.out.printf("LEARNING: maxdelta = %s%n", maxdelta);
        System.out.printf("maxLastTradeDelta = %s%n", maxLastTradeDelta);
//        simulateDays(symbol);
    }

    @Override
    public void learningSellPercent(String symbol) {
        double maxUsdt = 0;
        double maxsellPercent = 0, maxbuyPercent = 0, maxdelta = 0, maxLastTradeDelta = 0;
        for (Integer sellPercent = 80; sellPercent <= 95; sellPercent += 1) {
            tradingService.setSELL_PERCENT(sellPercent);

            tradingService.getUSDT().updateAndGet(v -> tradingService.getSTART_USDT());
            tradingService.getAmount().updateAndGet(v -> 0.0);
            tradingService.getTotalUsdt().updateAndGet(v -> tradingService.getSTART_USDT());
            simulateDays(symbol);
            System.out.printf("totalUsdt = %s", tradingService.getTotalUsdt().get());
            if (tradingService.getTotalUsdt().get() > maxUsdt) {
                maxUsdt = tradingService.getTotalUsdt().get();
                maxsellPercent = sellPercent;
                maxbuyPercent = tradingService.getBUY_PERCENT();
                maxdelta = tradingService.getDELTA();
            }
        }
        System.out.printf("maxUsdt = %s%n", maxUsdt);
        System.out.printf("LEARNING: sellPercent = %s%n", maxsellPercent);
        System.out.printf("buyPercent = %s%n", maxbuyPercent);
        System.out.printf("maxdelta = %s%n", maxdelta);
        System.out.printf("maxLastTradeDelta = %s%n", maxLastTradeDelta);
//        simulateDays(symbol);
    }

    @Override
    public void learningBuyPercent(String symbol) {
        double maxUsdt = 0;
        double maxsellPercent = 0, maxbuyPercent = 0, maxdelta = 0, maxLastTradeDelta = 0;
        for (Integer buyPercent = 1; buyPercent <= 20; buyPercent += 1) {
            tradingService.setBUY_PERCENT(buyPercent);

            tradingService.getUSDT().updateAndGet(v -> tradingService.getSTART_USDT());
            tradingService.getAmount().updateAndGet(v -> 0.0);
            tradingService.getTotalUsdt().updateAndGet(v -> tradingService.getSTART_USDT());
            simulateDays(symbol);
            System.out.printf("totalUsdt = %s", tradingService.getTotalUsdt().get());
            if (tradingService.getTotalUsdt().get() > maxUsdt) {
                maxUsdt = tradingService.getTotalUsdt().get();
                maxsellPercent = tradingService.getSELL_PERCENT();
                maxbuyPercent = buyPercent;
                maxdelta = tradingService.getDELTA();
            }
        }
        System.out.printf("maxUsdt = %s%n", maxUsdt);
        System.out.printf("sellPercent = %s%n", maxsellPercent);
        System.out.printf("LEARNING: buyPercent = %s%n", maxbuyPercent);
        System.out.printf("maxdelta = %s%n", maxdelta);
        System.out.printf("maxLastTradeDelta = %s%n", maxLastTradeDelta);
//        simulateDays(symbol);
    }

    @Override
    public void simulateDays(String symbol) {
        ArrayList<String> files = new ArrayList<>();
//        files.add("DOGEUSDT-2021-06-16");
//        files.add("DOGEUSDT-2021-06-17");
//        files.add("DOGEUSDT-2021-07-02");
//        files.add("DOGEUSDT-2021-07-17");
//        files.add("DOGEUSDT-2021-07-20");
//        files.add("DOGEUSDT-2021-07-21-2");
//        files.add("DOGEUSDT-2021-07-21");
//        files.add("DOGEUSDT-2021-07-22");
//        files.add("DOGEUSDT-2021-07-23");
//        files.add("DOGEUSDT-2021-07-24");
//        files.add("DOGEUSDT-2021-07-25");
//        files.add("DOGEUSDT-2021-07-31");
        files.add("DOGEUSDT-2021-08-06");
//        files.add("DOGEUSDT-2021-08-07");
        List<CandlestickEvent> candlesticks = new ArrayList<>();
        for (String filename : files) {
            System.out.println(filename);
            candlesticks = readResponses(symbol, filename);
            simulateResponses(symbol, candlesticks);
        }
        writeResult(candlesticks);
    }

    @Override
    public void simulateResponses(String symbol, List<CandlestickEvent> candlesticks) {
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        for (int i = 0; i < candlesticks.size() - 2; i++) {
            wave.setCandlestickEvent(candlesticks.get(i));
            tradingService.trade(wave);
        }
        wave.setWaveAction(WaveAction.SELL);
        tradingService.decision(wave.getWaveAction().getValue(), wave);
        writeResult(candlesticks);
    }

    private void writeResult(List<CandlestickEvent> candlesticks) {
        tradingService.getTotalUsdt().updateAndGet(v->tradingService.getUSDT().get() + tradingService.getAmount().get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
        System.out.printf("total traded usdt = %s%n", tradingService.getTotalUsdt().get());
        System.out.printf("usdt passive= %s%n",
                tradingService.getUSDT().get() * (Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose())
                        / Double.parseDouble(candlesticks.get(0).getClose())));
    }

    @Override
    public List<CandlestickEvent> readResponses(String symbol, String filename) {
        ArrayList<CandlestickEvent> candlesticks = new ArrayList<>();
        try {
            File file = new File("responses/" + filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String[] line = myReader.nextLine().split(" ");
                CandlestickEvent candlestick = new CandlestickEvent();
                candlestick.setClose(line[0]);
                candlestick.setQuoteAssetVolume(line[1]);
                candlesticks.add(candlestick);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return candlesticks;
    }
}
