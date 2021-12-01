package com.crypto.services.impl.angle;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.crypto.controllers.WaveDto;
import com.crypto.enums.WaveStatus;
import com.crypto.indicators.EMA;
import com.crypto.services.FuturesSimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class FuturesSimulatorServiceImpl implements FuturesSimulatorService {
    private final FuturesServiceImpl futuresService;

    @Override
    public void simulateDays(String symbol) {
        List<CandlestickEvent> candlesticks = readResponses(symbol);
        simulateResponses(symbol, candlesticks);
//        double max = 0;
//        int maxI = 0;
//        for (int i = 5; i < 500; i++){
//            futuresService.setCOUNTER_LIMIT(i);
//        simulateResponses(symbol, candlesticks);
//            System.out.println(i);
//            if (futuresService.getLAST_TRADE_USDT() > max){
//                max = futuresService.getLAST_TRADE_USDT();
//                maxI = i;
//                System.out.println(max);
//            }
//            futuresService.setCOUNTER(0);
//            futuresService.setUSDT_IN_TRADE(10);
//            futuresService.setTRADE_OPEN(0.0);
//            futuresService.setCURRENT_USDT(990.0);
//            futuresService.setLAST_TRADE_USDT(1000.0);
//            futuresService.setLAST_CLOSE(0.0);
//        }
//        System.out.println("maxI = " + maxI);
//        System.out.println("max = " + max);
    }

    @Override
    public void simulateResponses(String symbol, List<CandlestickEvent> candlesticks) {
        double min = 10000;
        double max = 0;
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        wave.setStatus(WaveStatus.WAIT);
        // 515, 90 = 5025 XRP
        wave.getEmas().add(new EMA(new ArrayList<>(), 515));
        wave.getEmas().add(new EMA(new ArrayList<>(), 90));
//        wave.getEmas().add(new EMA(new ArrayList<>(), 1000));
//        wave.getEmas().add(new EMA(new ArrayList<>(), 100));
//        wave.getEmas().add(new EMA(new ArrayList<>(), 90));
//        wave.getEmas().add(new EMA(new ArrayList<>(), 5));

        futuresService.setTRADE_OPEN(Double.parseDouble(candlesticks.get(0).getOpen()));
        for (int i = 0; i < candlesticks.size() - 2; i++) {
            futuresService.decision(candlesticks.get(i), wave);
            min = Math.min(futuresService.getLAST_TRADE_USDT(), min);
            max = Math.max(futuresService.getLAST_TRADE_USDT(), max);
        }
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        System.out.println("getCURRENT_USDT = " + futuresService.getLAST_TRADE_USDT());
    }

    @Override
    public List<CandlestickEvent> readResponses(String symbol) {
        ArrayList<CandlestickEvent> candlesticks = new ArrayList<>();
        try {
            File file = new File("statistics/" + symbol);
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
