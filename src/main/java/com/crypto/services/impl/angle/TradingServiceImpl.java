package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.crypto.dto.WaveDto;
import com.crypto.enums.WaveAction;
import com.crypto.services.TradingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Data
@RequiredArgsConstructor
public class TradingServiceImpl implements TradingService {
    private final BinanceApiClientFactory clientFactory;
    private final BinanceApiRestClient restClient;
    private final BinanceApiWebSocketClient webSocketClient;
    private final Double START_USDT = 1000.0;
    AtomicReference<Double> USDT = new AtomicReference<>(START_USDT);
    AtomicReference<Double> totalUsdt = new AtomicReference<>(START_USDT);
    AtomicReference<Double> amount = new AtomicReference<>(0.0);
    AtomicReference<Double> firstClose = new AtomicReference<>(0.0);
    AtomicReference<Double> lastClose = new AtomicReference<>(0.0);

    private final String SYMBOL = "DOGEUSDT";

    //    private Double delta = 0.003;
//    private final Double DELTA = 0.00476;
    private final Double DELTA = 0.0015;
    private final Double DELTA_DUMP = 0.0003;
    private final Double DELTA_PUMP = 0.0016;
    private final Integer SELL_PERCENT = 84;
    private final Integer BUY_PERCENT = 14;

    @Override
    public void decision(double decisionRate, WaveDto wave) {
        if (decisionRate > 0)
            buy(decisionRate, wave);
        else
            sell(decisionRate, wave);
    }

    @Override
    public double rate(WaveDto wave) {
        double responseClose = Double.parseDouble(wave.getCandlestickEvent().getClose());
        double onePercent = (wave.getHigh() - wave.getLow()) * 0.01;
        wave.setWaveAction(WaveAction.WAIT);
        wave.setClose(responseClose);

        // find trend
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
        // find extremum
        if (responseClose < wave.getLow()) {
            wave.setLow(responseClose);
            return wave.getWaveAction().getValue();
        }
        if (responseClose > wave.getHigh()) {
            wave.setHigh(responseClose);
            return wave.getWaveAction().getValue();
        }
        // trading
        if (wave.getHigh() - wave.getLow() > DELTA) {
            if (wave.getDumpSignal()) {
                if (responseClose >= wave.getLow() + onePercent * BUY_PERCENT) {
                    wave.setWaveAction(WaveAction.BUY);
                    wave.setHigh(responseClose);
                    return wave.getWaveAction().getValue();
                }
            }
            if (wave.getPumpSignal()) {
                if (responseClose <= wave.getLow() + onePercent * SELL_PERCENT) {
                    wave.setWaveAction(WaveAction.SELL);
                    wave.setLow(responseClose);
                    return wave.getWaveAction().getValue();
                }
            }
        }
        return wave.getWaveAction().getValue();
    }

    @Override
    public void trade(WaveDto wave) {
        double decisionRate = rate(wave);
        decision(decisionRate, wave);
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

            if (firstClose.get() == 0.0)
                firstClose.updateAndGet(v -> Double.valueOf(response.getClose()));

            lastClose.updateAndGet(v -> Double.valueOf(response.getClose()));
            trade(wave);

            writeResponse(wave);
        });
    }

    @Override
    public void writeResponse(WaveDto wave) {
        try {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            Double totalUSDT = USDT.get() + amount.get() * lastClose.get();
            Double passiveUSDT = START_USDT * (lastClose.get() / firstClose.get());

            BufferedWriter writer = new BufferedWriter(new FileWriter("trading/" + SYMBOL, true));
            String row = String.format("%s %s %s %s %s %s %s %s%n",
                    time, wave.getWaveAction(), wave.getClose(), wave.getLow(),
                    wave.getHigh(), totalUSDT, passiveUSDT, date);
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
    public void buy(double decisionRate, WaveDto wave) {
        double close = Double.parseDouble(wave.getCandlestickEvent().getClose());
        double delta = -USDT.get() * decisionRate;
        double deltaAmount = -delta / close;
        totalUsdt.updateAndGet(v -> USDT.get() + amount.get() * close);
        if (deltaAmount > 10) {
            USDT.updateAndGet(v -> v + delta + delta * 0.00075);
            amount.updateAndGet(v -> v + deltaAmount);
            totalUsdt.updateAndGet(v -> USDT.get() + amount.get() * close);
            LocalTime time = LocalTime.now();
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
                    wave.getWaveAction(), time, close, totalUsdt, delta, USDT, SYMBOL, amount);
        }
    }

    @Override
    public void sell(double decisionRate, WaveDto wave) {
        double close = Double.parseDouble(wave.getCandlestickEvent().getClose());
        double delta = -decisionRate * amount.get();
        totalUsdt.updateAndGet(v -> USDT.get() + amount.get() * close);
        if (delta > 10) {
            USDT.updateAndGet(v -> v + delta * close - delta * close * 0.00075);
            amount.updateAndGet(v -> v - delta);
            totalUsdt.updateAndGet(v -> USDT.get() + amount.get() * close);
            LocalTime time = LocalTime.now();
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, delta = %s, usdt = %s, %s = %s%n",
                    wave.getWaveAction(), time, close, totalUsdt, delta * close, USDT, SYMBOL, amount);
        }
    }

}