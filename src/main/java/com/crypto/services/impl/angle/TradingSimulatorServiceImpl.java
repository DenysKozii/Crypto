package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;
import com.crypto.services.TradingSimulatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class TradingSimulatorServiceImpl implements TradingSimulatorService {
    private final BinanceApiClientFactory clientFactory;
    private final BinanceApiRestClient restClient;
    private final Double USDT = 1000.0;
    AtomicReference<Double> usdt = new AtomicReference<>(USDT);
    AtomicReference<Double> totalUsdt = new AtomicReference<>(USDT);
    AtomicReference<Double> amount = new AtomicReference<>(0.0);
    private final String SYMBOL = "DOGEUSDT";

    //    private Integer SELL_PERCENT = 96;
//    private Integer BUY_PERCENT = 0;
    private Integer DNO_PERCENT = 51;

//    private Double delta = 0.00476;
//    private Double DELTA = 0.0015;
//    private Double DELTA_DUMP = 0.0003;
//    private Double DELTA_PUMP = 0.0016;

    private final Double DELTA = 0.0015;
    private final Double DELTA_DUMP = 0.0003;
    private final Double DELTA_PUMP = 0.0016;
    private final Integer SELL_PERCENT = 84;
    private final Integer BUY_PERCENT = 14;


    @Override
    public void learning(String symbol) {
//        double maxUsdt = 0;
//        double maxsellPercent = 0, maxbuyPercent = 0, maxdelta = 0, maxLastTradeDelta = 0;
//        for (DNO_PERCENT = -30; DNO_PERCENT <= 80; DNO_PERCENT += 1) {
////            for (buyPercent = 0; buyPercent <= 20; buyPercent += 1) {
////                for (delta = 0.002; delta <= 0.006; delta += 0.0001) {
//            usdt.set(USDT);
//            amount.set(0.0);
//            totalUsdt.set(USDT);
//            simulateDays(symbol);
//            System.out.printf("totalUsdt = %s, DNO_PERCENT = %s%n", totalUsdt, DNO_PERCENT);
//            if (totalUsdt.get() > maxUsdt) {
//                maxUsdt = totalUsdt.get();
//                maxsellPercent = SELL_PERCENT;
//                maxbuyPercent = BUY_PERCENT;
//                maxdelta = DNO_PERCENT;
////                        }
//            }
//        }
//        System.out.printf("maxUsdt = %s%n", maxUsdt);
//        System.out.printf("sellPercent = %s%n", maxsellPercent);
//        System.out.printf("buyPercent = %s%n", maxbuyPercent);
//        System.out.printf("maxdelta = %s%n", maxdelta);
//        System.out.printf("maxLastTradeDelta = %s%n", maxLastTradeDelta);
        simulateDays(symbol);
    }

    @Override
    public double rate(WaveDto wave, Candlestick response) {
        double responseClose = Double.parseDouble(response.getClose());
        double onePercent = (wave.getHigh() - wave.getLow()) * 0.01;
        wave.setWaveAction(WaveAction.WAIT);

        if (responseClose <= wave.getValue() - DELTA_DUMP) {
            wave.setDumpSignal(true);
            wave.setPumpSignal(false);
            wave.setValue(responseClose);
        }
        if (responseClose >= wave.getValue() + DELTA_PUMP) {
            wave.setDumpSignal(false);
            wave.setPumpSignal(true);
            wave.setValue(responseClose);
        }
//        if (responseClose <= wave.getActionPrice() - onePercent * DNO_PERCENT && responseClose <= wave.getLow()) {
//            wave.setWaveAction(WaveAction.SELL);
//        }
        if (responseClose < wave.getLow()) {
            wave.setLow(responseClose);
            return wave.getWaveAction().getValue();
        }
        if (responseClose > wave.getHigh()) {
            wave.setHigh(responseClose);
            return wave.getWaveAction().getValue();
        }
        if (wave.getHigh() - wave.getLow() > DELTA) {
            if (wave.getDumpSignal()) {
                if (responseClose >= wave.getLow() + onePercent * BUY_PERCENT) {
                    wave.setWaveAction(WaveAction.BUY);
                    wave.setHigh(responseClose);
                    wave.setActionPrice(responseClose);
                    return wave.getWaveAction().getValue();
                }
            }
            if (wave.getPumpSignal()) {
                if (responseClose <= wave.getLow() + onePercent * SELL_PERCENT) {
                    wave.setWaveAction(WaveAction.SELL);
                    wave.setLow(responseClose);
                    wave.setActionPrice(responseClose);
                    return wave.getWaveAction().getValue();
                }
            }
        }

        return wave.getWaveAction().getValue();
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
//        files.add("DOGEUSDT-2021-08-06");
        files.add("DOGEUSDT-2021-08-07");
        List<Candlestick> candlesticks = new ArrayList<>();
        for (String filename : files) {
            System.out.println(filename);
            candlesticks = readResponses(symbol, filename);
            simulateResponses(symbol, candlesticks);
        }
        writeResult(candlesticks);
    }


    @Override
    public void simulateResponses(String symbol, List<Candlestick> candlesticks) {
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        for (int i = 0; i < candlesticks.size() - 2; i++) {
            Candlestick candlestick = candlesticks.get(i);
            trade(wave, candlestick);
        }
        wave.setWaveAction(WaveAction.SELL);
        decision(wave.getWaveAction().getValue(), wave, candlesticks.get(candlesticks.size() - 1));
        writeResult(candlesticks);
    }

    private void writeResult(List<Candlestick> candlesticks) {
        totalUsdt.set(usdt.get() + amount.get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
        System.out.printf("total traded usdt = %s%n", totalUsdt);
        System.out.printf("usdt passive= %s%n",
                USDT * (Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose())
                        / Double.parseDouble(candlesticks.get(0).getClose())));
    }

    @Override
    public void decision(double decisionRate, WaveDto wave, Candlestick response) {
        if (decisionRate > 0)
            buy(decisionRate, wave, response);
        else
            sell(decisionRate, wave, response);
    }

    @Override
    public void trade(WaveDto wave, Candlestick response) {
        double decisionRate = rate(wave, response);
        decision(decisionRate, wave, response);
    }

    @Override
    public List<Candlestick> readResponses(String symbol, String filename) {
        ArrayList<Candlestick> candlesticks = new ArrayList<>();
        try {
            File file = new File("responses/" + filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String[] line = myReader.nextLine().split(" ");
                Candlestick candlestick = new Candlestick();
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

    @Override
    public void buy(double decisionRate, WaveDto wave, Candlestick response) {
        double close = Double.parseDouble(response.getClose());
        double delta = -usdt.get() * decisionRate;
        double deltaAmount = -delta / close;
        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
        if (deltaAmount > 10) {
            usdt.updateAndGet(v -> v + delta + delta * 0.00075);
            amount.updateAndGet(v -> v + deltaAmount);
            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
            System.out.printf("%s: time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
                    wave.getWaveAction(), response.getQuoteAssetVolume(), close, totalUsdt, delta, usdt, SYMBOL, amount);
        }
    }

    @Override
    public void sell(double decisionRate, WaveDto wave, Candlestick response) {
        double close = Double.parseDouble(response.getClose());
        double delta = -decisionRate * amount.get();
        totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
        if (delta > 10) {
            usdt.updateAndGet(v -> v + delta * close - delta * close * 0.00075);
            amount.updateAndGet(v -> v - delta);
            totalUsdt.updateAndGet(v -> usdt.get() + amount.get() * close);
            System.out.printf("%s: time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
                    wave.getWaveAction(), response.getQuoteAssetVolume(), close, totalUsdt, delta * close, usdt, SYMBOL, amount);
        }
    }
}
