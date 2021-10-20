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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;

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

    private Double DELTA_DUMP = 0.999;
    private Double DELTA_PUMP = 1.001;


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
        String time = logTime(wave.getCandlestickEvent().getOpenTime()).substring(9, 14);
        if (responseClose < wave.getValue() * DELTA_DUMP) {
            if (WaveStatus.PUMP.equals(wave.getStatus())) {
                wave.setAction(WaveAction.SELL);
            }
            wave.setStatus(WaveStatus.DUMP);
            wave.setValue(responseClose);
        }
        if (responseClose > wave.getValue() * DELTA_PUMP) {
            if (WaveStatus.DUMP.equals(wave.getStatus())) {
                wave.setAction(WaveAction.BUY);
            }
            wave.setStatus(WaveStatus.PUMP);
            wave.setValue(responseClose);
        }
        return wave.getAction().getValue();
    }

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
        if (simulate) {
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, usdt = %s, %s = %s%n",
                    wave.getAction(), logTime(wave.getCandlestickEvent().getCloseTime()), wave.getClose(), totalUsdt, USDT, SYMBOL, amount);
        } else {
            System.out.printf("%s:time = %s, price = %s, total usdt = %s, usdt = %s, %s = %s%n",
                    wave.getAction(), time, wave.getClose(), totalUsdt, USDT, SYMBOL, amount);
        }
    }

    private String logTime(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        return df.format(currentDate);
    }
}