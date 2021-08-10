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
    private final String SYMBOL = "DOGEUSDT";


    @Override
    public void learning(String symbol) {
        double maxUsdt = 0;
        double MAX_DELTA_DUMP = 0, MAX_DELTA_PUMP = 0;
        for (double DELTA_PUMP = 0.0; DELTA_PUMP <= 0.007; DELTA_PUMP += 0.0001) {
            for (double DELTA_DUMP = 0.0; DELTA_DUMP <= 0.006; DELTA_DUMP += 0.0001) {
                tradingService.setDELTA_PUMP(DELTA_PUMP);
                tradingService.setDELTA_DUMP(DELTA_DUMP);
                tradingService.setUSDT(tradingService.getSTART_USDT());
                tradingService.setAmount(0.0);
                tradingService.setTotalUsdt(tradingService.getSTART_USDT());
                simulateDays(symbol);
                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
                System.out.printf("totalUsdt = %s, DELTA_PUMP = %f, DELTA_DUMP = %f%n", tradingService.getTotalUsdt(), DELTA_PUMP, DELTA_DUMP);
                if (tradingService.getTotalUsdt() > maxUsdt) {
                    maxUsdt = tradingService.getTotalUsdt();
                    MAX_DELTA_DUMP = DELTA_DUMP;
                    MAX_DELTA_PUMP = DELTA_PUMP;
                }
            }
        }
        tradingService.setDELTA_PUMP(MAX_DELTA_PUMP);
        tradingService.setDELTA_DUMP(MAX_DELTA_DUMP);
        tradingService.setUSDT(tradingService.getSTART_USDT());
        tradingService.setAmount(0.0);
        tradingService.setTotalUsdt(tradingService.getSTART_USDT());
        simulateDays(symbol);
        System.out.printf("maxUsdt = %f%n", maxUsdt);
        System.out.printf("MAX_DELTA_PUMP = %f%n", MAX_DELTA_PUMP);
        System.out.printf("MAX_DELTA_DUMP = %f%n", MAX_DELTA_DUMP);
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
            writeResult(candlesticks);
        }
        writeResult(candlesticks);
    }

    @Override
    public void simulateResponses(String symbol, List<CandlestickEvent> candlesticks) {
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        for (int i = 0; i < candlesticks.size() - 2; i++) {
            wave.setCandlestickEvent(candlesticks.get(i));
            tradingService.trade(wave, true);
        }
        wave.setAction(WaveAction.SELL);
        tradingService.decision(wave.getAction().getValue(), wave, true);
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
