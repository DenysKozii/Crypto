package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;
import com.crypto.enums.WaveStatus;
import com.crypto.services.TradingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

@Service
@Data
@RequiredArgsConstructor
public class TradingServiceImpl implements TradingService {
    private final BinanceApiClientFactory clientFactory;
    private final BinanceApiRestClient restClient;
    private final BinanceApiWebSocketClient webSocketClient;
    private final Double START_USDT = 1000.0;
    private final Double TAX = 0.00075;

    private Double USDT = START_USDT;
    private Double totalUsdt = START_USDT;
    private Double amount = 0.0;
    private Double firstClose = 0.0;
    private Double lastClose = 0.0;


    private final String SYMBOL = "DOGEUSDT";

//    private final Integer SELL_PERCENT = 84;
//    private final Integer BUY_PERCENT = 14;
    //    private Double delta = 0.003;
//    private final Double DELTA = 0.00476;

//    private final Double DELTA = 0.0025;

//    private final Double DELTA_DUMP = 0.0003;
//    private final Double DELTA_PUMP = 0.0016;

    private Double DELTA_DUMP = 0.001;
    private Double DELTA_PUMP = 0.001;


    @Override
    public void decision(double decisionRate, WaveDto wave, boolean simulate) {
        if (decisionRate > 0)
            buy(decisionRate, wave, simulate);
        else
            sell(decisionRate, wave, simulate);
    }

    @Override
    public double rate(WaveDto wave) {
        double responseClose = Double.parseDouble(wave.getCandlestickEvent().getClose());
        wave.setAction(WaveAction.WAIT);
        wave.setClose(responseClose);
        if (responseClose <= wave.getValue() - DELTA_DUMP) {
            if (WaveStatus.PUMP.equals(wave.getStatus())) {
//                System.out.printf("%s %s pump->dump%n",
//                        wave.getCandlestickEvent().getQuoteAssetVolume(), wave.getClose());
                wave.setAction(WaveAction.SELL);
            }
            wave.setStatus(WaveStatus.DUMP);
            wave.setValue(responseClose);
        }
        if (responseClose >= wave.getValue() + DELTA_PUMP) {
            if (WaveStatus.DUMP.equals(wave.getStatus())) {
//                System.out.printf("%s %s dump->pump%n",
//                        wave.getCandlestickEvent().getQuoteAssetVolume(), wave.getClose());
                wave.setAction(WaveAction.BUY);
            }
            wave.setStatus(WaveStatus.PUMP);
            wave.setValue(responseClose);
        }
        return wave.getAction().getValue();
    }

//    @Override
//    public double rate(WaveDto wave) {
//        double responseClose = Double.parseDouble(wave.getCandlestickEvent().getClose());
//        double onePercent = (wave.getHigh() - wave.getLow()) * 0.01;
//        wave.setWaveAction(WaveAction.WAIT);
//        wave.setClose(responseClose);
//
//        // find trend
//        if (responseClose <= wave.getValue() - DELTA_DUMP) {
//            if(wave.getPumpSignal())
//                System.out.printf("%s %s pump->dump%n",
//                        wave.getCandlestickEvent().getQuoteAssetVolume(), wave.getClose());
//            wave.setDumpSignal(true);
//            wave.setPumpSignal(false);
//            wave.setValue(responseClose);
//        }
//        if (responseClose >= wave.getValue() + DELTA_PUMP) {
//            if(wave.getDumpSignal())
//                System.out.printf("%s %s dump->pump%n",
//                        wave.getCandlestickEvent().getQuoteAssetVolume(), wave.getClose());
//            wave.setDumpSignal(false);
//            wave.setPumpSignal(true);
//            wave.setValue(responseClose);
//        }
//        // find extremum
//        if (responseClose < wave.getLow()) {
//            wave.setLow(responseClose);
//            return wave.getWaveAction().getValue();
//        }
//        if (responseClose > wave.getHigh()) {
//            wave.setHigh(responseClose);
//            return wave.getWaveAction().getValue();
//        }
//        // trading
//        if (wave.getHigh() - wave.getLow() > DELTA) {
//            if (wave.getDumpSignal()) {
//                if (responseClose >= wave.getLow() + onePercent * BUY_PERCENT && !wave.getBought()) {
//                    wave.setWaveAction(WaveAction.BUY);
//                    wave.setHigh(responseClose);
//                    wave.setBought(true);
//                    return wave.getWaveAction().getValue();
//                }
//            }
//            if (wave.getPumpSignal()) {
//                if (responseClose <= wave.getLow() + onePercent * SELL_PERCENT) {
//                    wave.setWaveAction(WaveAction.SELL);
//                    wave.setLow(responseClose);
//                    wave.setBought(false);
//                }
//                return wave.getWaveAction().getValue();
//            }
//        }
//        return wave.getWaveAction().getValue();
//    }

    @Override
    public void trade(WaveDto wave, boolean simulate) {
        double decisionRate = rate(wave);
        decision(decisionRate, wave, simulate);
    }

    @Override
    public void startTrading(String symbol) {
        WaveDto wave = new WaveDto();
        wave.setSymbol(symbol);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("trading/" + symbol));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        webSocketClient.onCandlestickEvent(symbol.toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {

            wave.setCandlestickEvent(response);

            if (firstClose == 0.0)
                firstClose = Double.valueOf(response.getClose());

            lastClose = Double.valueOf(response.getClose());
            trade(wave, false);

            writeResponse(wave);
        });
    }

    @Override
    public void writeResponse(WaveDto wave) {
        try {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            Double totalUSDT = USDT + amount * lastClose;
            Double passiveUSDT = START_USDT * (lastClose / firstClose);

            BufferedWriter writer = new BufferedWriter(new FileWriter("trading/" + SYMBOL, true));
            String row = String.format("%s %s %s %s %s %s %s%n",
                    time, wave.getAction(), wave.getClose(),
                    totalUSDT, passiveUSDT, wave.getStatus(), date);
            writer.write(row);
            writer.close();

            BufferedWriter writer2 = new BufferedWriter(new FileWriter("responses/" + wave.getSymbol() + "-" + date.toString(), true));
            String row2 = String.format("%s %s%n",
                    wave.getClose(), time);
            writer2.write(row2);
            writer2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void buy(double decisionRate, WaveDto wave, boolean simulate) {
        double close = Double.parseDouble(wave.getCandlestickEvent().getClose());
        double delta = -USDT * decisionRate;
        double deltaAmount = -delta / close;
        totalUsdt = USDT + amount * close;
        if (deltaAmount > 10) {
            USDT += delta + delta * TAX;
            amount += deltaAmount;
            totalUsdt = USDT + amount * close;
            writeAction(wave, simulate);
        }
    }

    @Override
    public void sell(double decisionRate, WaveDto wave, boolean simulate) {
        double close = Double.parseDouble(wave.getCandlestickEvent().getClose());
        double delta = -decisionRate * amount;
        totalUsdt = USDT + amount * close;
        if (delta > 10) {
            USDT += delta * close - delta * close * TAX;
            amount -= delta;
            totalUsdt = USDT + amount * close;
            writeAction(wave, simulate);
        }
    }

    @Override
    public void writeAction(WaveDto wave, boolean simulate) {
        LocalTime time = LocalTime.now();
        if (simulate)
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, usdt = %s, %s = %s%n",
                    wave.getAction(), wave.getCandlestickEvent().getQuoteAssetVolume(), wave.getClose(), totalUsdt, USDT, SYMBOL, amount);
        else
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, usdt = %s, %s = %s%n",
                    wave.getAction(), time, wave.getClose(), totalUsdt, USDT, SYMBOL, amount);
    }
}