package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.controllers.WaveDto;
import com.crypto.enums.WaveAction;
import com.crypto.indicators.EMA;
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


    @Override
    public void learning(String symbol, boolean simulate) {
        double maxUsdt = 0;
        int MAX_PERIOD = 0;
//        for (int period = 1; period < 220; period++) {
//            tradingService.setUSDT(tradingService.getSTART_USDT());
//            tradingService.setAmount(0.0);
//            tradingService.setTotalUsdt(tradingService.getSTART_USDT());
//            simulateDays(symbol, simulate);
//            System.out.printf("totalUsdt = %s, PERIOD = %s%n", tradingService.getTotalUsdt(), period);
//            System.out.printf("totalUsdt = %s, PERIOD = %s%n", tradingService.getTotalUsdt(), period);
//            System.out.printf("totalUsdt = %s, PERIOD = %s%n", tradingService.getTotalUsdt(), period);
//            if (tradingService.getTotalUsdt() > maxUsdt) {
//                maxUsdt = tradingService.getTotalUsdt();
//                MAX_PERIOD = period;
//            }
//        }
//        double MAX_DELTA_DUMP = 0, MAX_DELTA_PUMP = 0;
//        for (double DELTA_PUMP = 1.006750; DELTA_PUMP <= 1.006750; DELTA_PUMP += 0.00025) {
//            for (double DELTA_DUMP = 0.972500; DELTA_DUMP >= 0.972500; DELTA_DUMP -= 0.0025) {
//                tradingService.setDELTA_PUMP(DELTA_PUMP);
//                tradingService.setDELTA_DUMP(DELTA_DUMP);
//                tradingService.setUSDT(tradingService.getSTART_USDT());
//                tradingService.setAmount(0.0);
//                tradingService.setTotalUsdt(tradingService.getSTART_USDT());
//                simulateDays(symbol, simulate);
//                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
//                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
//                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
//                if (tradingService.getTotalUsdt() > maxUsdt) {
//                    maxUsdt = tradingService.getTotalUsdt();
//                    MAX_DELTA_DUMP = DELTA_DUMP;
//                    MAX_DELTA_PUMP = DELTA_PUMP;
//                }
//            }
//        }
//        tradingService.setDELTA_PUMP(MAX_DELTA_PUMP);
//        tradingService.setDELTA_DUMP(MAX_DELTA_DUMP);
//        tradingService.setUSDT(tradingService.getSTART_USDT());
//        tradingService.setAmount(0.0);
//        tradingService.setTotalUsdt(tradingService.getSTART_USDT());
//        period = MAX_PERIOD;
        simulateDays(symbol, simulate);
        System.out.printf("maxUsdt = %f%n", maxUsdt);
//        System.out.printf("MAX_DELTA_DUMP = %f%n", MAX_DELTA_DUMP);
    }

    @Override
    public void simulateDays(String symbol, boolean simulate) {
        ArrayList<String> files = new ArrayList<>();
        files.add(symbol);
        List<CandlestickEvent> candlesticks = new ArrayList<>();
        for (String filename : files) {
            System.out.println(filename);
            candlesticks = readResponses(symbol, filename);
//            candlesticks = candlesticks.subList(candlesticks.size(), candlesticks.size());
            simulateResponses(symbol, candlesticks, simulate);
            writeResult(candlesticks);
        }
        writeResult(candlesticks);
    }

    @Override
    public void simulateResponses(String symbol, List<CandlestickEvent> candlesticks, boolean simulate) {
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        // 1000, 25 = 1901
//        wave.getEmas().add(new EMA(new ArrayList<>(), 1000));
        wave.getEmas().add(new EMA(new ArrayList<>(), 1));
        double min = 10000;
        double max = 0;
        for (int i = 0; i < candlesticks.size() - 2; i++) {
            wave.setCandlestickEvent(candlesticks.get(i));
            tradingService.trade(wave, simulate);
            min = Math.min(tradingService.getTotalUsdt(), min);
            max = Math.max(tradingService.getTotalUsdt(), max);
        }
        wave.setAction(WaveAction.SELL);
        tradingService.decision(wave.getAction().getValue(), wave, simulate);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
//        writeResult(candlesticks);
    }

    private void writeResult(List<CandlestickEvent> candlesticks) {
        tradingService.setTotalUsdt(tradingService.getUSDT() + tradingService.getAmount() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
        System.out.printf("total traded usdt = %s%n", tradingService.getTotalUsdt());
        System.out.printf("usdt passive = %s%n",
                tradingService.getSTART_USDT() * (Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose())
                        / Double.parseDouble(candlesticks.get(0).getClose())));
    }

    @Override
    public List<CandlestickEvent> readResponses(String symbol, String filename) {
        ArrayList<CandlestickEvent> candlesticks = new ArrayList<>();
        try {
            File file = new File("statistics/" + filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                CandlestickEvent candlestick = mapToCandlestick(line);
                candlesticks.add(candlestick);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return candlesticks;
    }

    private CandlestickEvent mapToCandlestick(String line) {
        String[] strings = line.split(" ");
        CandlestickEvent candlestick = new CandlestickEvent();
        candlestick.setOpenTime(Long.valueOf(strings[0]));
        candlestick.setOpen(strings[1]);
        candlestick.setHigh(strings[2]);
        candlestick.setLow(strings[3]);
        candlestick.setClose(strings[4]);
        candlestick.setVolume(strings[5]);
        candlestick.setCloseTime(Long.valueOf(strings[6]));
        candlestick.setQuoteAssetVolume(strings[7]);
        candlestick.setNumberOfTrades(Long.valueOf(strings[8]));
        candlestick.setTakerBuyBaseAssetVolume(strings[9]);
        candlestick.setTakerBuyQuoteAssetVolume(strings[10]);
        return candlestick;
    }
}
