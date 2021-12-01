package com.crypto.services.impl.angle;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.binance.api.client.domain.market.TickerPrice;
import com.binance.api.client.domain.market.TickerStatistics;
import com.crypto.controllers.WaveDto;
import com.crypto.dto.ShortDto;
import com.crypto.dto.TradeDto;
import com.crypto.enums.WaveStatus;
import com.crypto.indicators.EMA;
import com.crypto.services.FuturesService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Data
public class FuturesServiceImpl implements FuturesService {
    private final BinanceApiWebSocketClient webSocketClient;
    private final BinanceApiRestClient restClient;

    private final Double START_USDT = 1000.0;
    private final Integer LEVEL_1 = 100;
    private final Integer LEVEL_2 = 100;
    private final Integer LEVEL_3 = 100;
    private final Integer LEVEL_4 = 100;
    private final Integer LEVEL_5 = 100;
    private final Integer LEVEL_6 = 100;
    //    private final Integer LEVEL_1 = 10;
//    private final Integer LEVEL_2 = 30;
//    private final Integer LEVEL_3 = 90;
//    private final Integer LEVEL_4 = 150;
//    private final Integer LEVEL_5 = 350;
//    private final Integer LEVEL_6 = 750;
    private final Double TAX = 0.0008;
    private final Integer SHOULDER = 10;

    private Integer USDT_IN_TRADE = 0;
    private Double CURRENT_USDT = START_USDT - USDT_IN_TRADE;
    private Double TOTAL_USDT = CURRENT_USDT;
    private Double LAST_TRADE_USDT = START_USDT;
    private Integer LAST_LEVEL = 0;
    private Integer COUNTER = 0;
    private Integer COUNTER_LIMIT = 0;
    private Double TRADE_OPEN = 0.0;
    private Double LAST_TRADE_DELTA = 0.0;
    private Boolean LONG = false;
    private Double VALUE = 0.0;
    //    private Double DELTA_DUMP = 0.97;
//    private Double DELTA_PUMP = 1.04;
    private Double DELTA_DUMP = 0.98;
    private Double DELTA_PUMP = 1.02;

    private HashMap<String, ShortDto> shorts = new HashMap<>();
    private HashMap<String, String> longs = new HashMap<>();

    private Double totalGainers = 0.0;

    private void fixPrice(ShortDto shortDto, double currentPrice) {
        double delta = 1 + (1 - currentPrice / shortDto.getOldClose()) * SHOULDER;
        delta = delta < -1 ? -1 : delta;
        CURRENT_USDT = CURRENT_USDT + shortDto.getTradeUSDT() * delta - shortDto.getTradeUSDT() - shortDto.getTradeUSDT() * SHOULDER * TAX;
        shortDto.setTradeUSDT(0.0);
    }

    private void recalculatePrice(ShortDto shortDto, double currentPrice) {
        double delta = 1 + (1 - currentPrice / shortDto.getOldClose()) * SHOULDER;
        delta = delta < -1 ? -1 : delta;
        TOTAL_USDT = CURRENT_USDT + shortDto.getTradeUSDT() * delta - shortDto.getTradeUSDT() - shortDto.getTradeUSDT() * SHOULDER * TAX;
    }

    @Override
    public void enterShorts(List<TickerStatistics> statistics) {
        System.out.println("TOP GAINERS:");
        List<String> newSymbols = statistics.stream().map(TickerStatistics::getSymbol).collect(Collectors.toList());
        double total = 0;
        for (Map.Entry<String, ShortDto> e : shorts.entrySet()) {
            TickerPrice price = restClient.getPrice(e.getKey());
            double currentPrice = Double.parseDouble(price.getPrice());
            double oldPrice = e.getValue().getOldClose();
            double percent = (1 - oldPrice / currentPrice) * 100;
            total += percent;
            System.out.println(e.getKey() + ": with enter = " + e.getValue().getOldClose() + " and current = " + price.getPrice() + ", percentage = " + percent);
//            if(!newSymbols.contains(e.getKey())) {
                fixPrice(e.getValue(), currentPrice);
//                shorts.remove(e.getKey());
//            } else {
//                recalculatePrice(e.getValue(), currentPrice);
//            }
        }
        shorts.clear();
        totalGainers += total;
        System.out.println("Total per trade = " + total);
        System.out.println("totalGainers = " + totalGainers);
        System.out.println("CURRENT_USDT = " + CURRENT_USDT);
//        statistics.stream()
//                .filter(o->!shorts.containsKey(o.getSymbol()))
//                .forEach(o -> shorts.put(o.getSymbol(),
//                        new ShortDto(Double.parseDouble(restClient.getPrice(o.getSymbol()).getPrice()), 100.0)));statistics.stream()
        statistics.forEach(o -> shorts.put(o.getSymbol(), new ShortDto(Double.parseDouble(restClient.getPrice(o.getSymbol()).getPrice()), CURRENT_USDT/10)));
    }

    @Override
    public void enterLongs(List<TickerStatistics> statistics) {
        System.out.println("GAINERS: ");
        double total = 0;
        for (Map.Entry<String, String> e : longs.entrySet()) {
            TickerPrice price = restClient.getPrice(e.getKey());
            double currentPrice = Double.parseDouble(price.getPrice());
            double oldPrice = Double.parseDouble(e.getValue());
            double percent = (1 - currentPrice / oldPrice) * 100;
            total += percent;
            System.out.println(e.getKey() + ": with enter = " + e.getValue() + " and current = " + price.getPrice() + ", percentage = " + percent);
        }
        totalGainers += total;
        System.out.println("Total = " + total);
        System.out.println("totalGainers = " + totalGainers);
        longs.clear();
        statistics.forEach(o -> longs.put(o.getSymbol(), restClient.getPrice(o.getSymbol()).getPrice()));
    }

    @Override
    public void subscription(String symbol) {
        TradeDto trade = new TradeDto();
        webSocketClient.onCandlestickEvent(symbol.toLowerCase(), CandlestickInterval.ONE_MINUTE, (CandlestickEvent response) -> {
            LocalTime time = LocalTime.now();
            trade.insertCandlestick(response);
            double close = Double.parseDouble(response.getClose());
            if (close < DELTA_DUMP * trade.getOldClose() && !trade.isTrade()) {
                trade.setOpen(close);
                trade.setTrade(true);
                trade.setWaveStatus(WaveStatus.PUMP);
                trade.setBuy(close);
//                buyLong(response, false, false);
                System.out.println(symbol + ": time = " + time + ", BUY LONG = " + close / trade.getOldClose());
            }
            if (close > DELTA_PUMP * trade.getOldClose() && !trade.isTrade()) {
                trade.setOpen(close);
                trade.setTrade(true);
                trade.setWaveStatus(WaveStatus.DUMP);
                trade.setBuy(close);
//                sellShort(response, false, false);
                System.out.println(symbol + ": time = " + time + ", SELL SHORT = " + close / trade.getOldClose());
            }
            if (trade.isTrade()) {
                if (trade.getWaveStatus().equals(WaveStatus.DUMP) && close < trade.getOpen() * DELTA_DUMP) {
                    trade.setTrade(false);
//                    fix(response);
                    System.out.println(symbol + ": time = " + time + " close short from " + trade.getBuy() + " to " + close);
                }
                if (trade.getWaveStatus().equals(WaveStatus.PUMP) && close > trade.getOpen() * DELTA_PUMP) {
                    trade.setTrade(false);
//                    fix(response);
                    System.out.println(symbol + ": time = " + time + " close long from " + trade.getBuy() + " to " + close);
                }
            }
        });
    }

    @Override
    public void decision(CandlestickEvent candlestickEvent, WaveDto wave) {
        double responseClose = Double.parseDouble(candlestickEvent.getClose());
        String time = logTime(candlestickEvent.getCloseTime());
//        System.out.println(time + ": PRICE = " + responseClose + ", VALUE * DELTA_DUMP = "  + VALUE * DELTA_DUMP + ", VALUE * DELTA_PUMP = " + VALUE * DELTA_PUMP + ", VALUE = " + VALUE);
        wave.getEmas().forEach(o -> o.update(responseClose));
        boolean readyForTrade = wave.getEmas().stream()
                .noneMatch(o -> o.getIterations() < o.getPeriod() * 5);
        if (readyForTrade) {
            boolean pump = true;
            boolean dump = true;
            double lastEma = 0;
            for (EMA ema : wave.getEmas()) {
                if (ema.get() < lastEma) {
                    pump = false;
                    break;
                }
                lastEma = ema.get();
            }
            lastEma = Double.MAX_VALUE;
            for (EMA ema : wave.getEmas()) {
                if (ema.get() > lastEma) {
                    dump = false;
                    break;
                }
                lastEma = ema.get();
            }

            if (responseClose > VALUE * DELTA_PUMP) {
                if (!LONG && pump) {
                    fix(candlestickEvent);
//                    writeAction(candlestickEvent, "OPEN LONG");
                    writeAction(candlestickEvent, "OPEN SHORT");
//                    buyLong(candlestickEvent, pump, dump);
                    sellShort(candlestickEvent, pump, dump);
                }
                if (!LONG && dump) {
                    fix(candlestickEvent);
                    LONG = true;
                }
                VALUE = responseClose;
            }
            if (responseClose < VALUE * DELTA_DUMP) {
                if (LONG && dump) {
                    fix(candlestickEvent);
//                    writeAction(candlestickEvent, "OPEN SHORT");
//                    sellShort(candlestickEvent, pump, dump);
                    writeAction(candlestickEvent, "OPEN LONG");
                    buyLong(candlestickEvent, pump, dump);
                }
                if (LONG && pump) {
                    fix(candlestickEvent);
                    LONG = false;
                }
                VALUE = responseClose;
            }
//            if (pump) {
//                if (!wave.getStatus().equals(WaveStatus.PUMP)) {
//                    buyLong(candlestickEvent);
//                    wave.setStatus(WaveStatus.PUMP);
//                }
//            } else if (dump) {
//                if (!wave.getStatus().equals(WaveStatus.DUMP)) {
//                    sellShort(candlestickEvent);
//                    wave.setStatus(WaveStatus.DUMP);
//                }
//            } else {
//                if (!wave.getStatus().equals(WaveStatus.WAIT)) {
//                    fix(candlestickEvent);
//                    wave.setStatus(WaveStatus.WAIT);
//                }
//            }
        }
    }



    @Override
    public void fix(CandlestickEvent candlestickEvent) {
        double currentClose = Double.parseDouble(candlestickEvent.getClose());
        double delta = LONG ? currentClose / TRADE_OPEN : TRADE_OPEN / currentClose;
        delta = 1 + (delta - 1) * SHOULDER;
        CURRENT_USDT = CURRENT_USDT + USDT_IN_TRADE * delta - USDT_IN_TRADE * SHOULDER * TAX;
        LAST_TRADE_DELTA = CURRENT_USDT - LAST_TRADE_USDT;
        LAST_TRADE_USDT = CURRENT_USDT;
        if (USDT_IN_TRADE != 0) {
            if (!LONG) {
                writeAction(candlestickEvent, "CLOSE LONG");
            } else {
                writeAction(candlestickEvent, "CLOSE SHORT");
            }
        }
        USDT_IN_TRADE = 0;
    }



    @Override
    public void buyLong(CandlestickEvent candlestickEvent, boolean pump, boolean dump) {
        double currentClose = Double.parseDouble(candlestickEvent.getClose());
        action();
        CURRENT_USDT -= USDT_IN_TRADE;
        TRADE_OPEN = currentClose;
        LONG = false;
    }

    @Override
    public void sellShort(CandlestickEvent candlestickEvent, boolean pump, boolean dump) {
        double currentClose = Double.parseDouble(candlestickEvent.getClose());
        action();
        CURRENT_USDT -= USDT_IN_TRADE;
        TRADE_OPEN = currentClose;
        LONG = true;
    }

    private void action() {
        // todo write if for current with flag
        if (LAST_TRADE_DELTA < 0) {
            if (LAST_LEVEL == 0) {
                USDT_IN_TRADE = Math.min(LEVEL_1, CURRENT_USDT.intValue());
            } else if (LAST_LEVEL.equals(LEVEL_1)) {
                USDT_IN_TRADE = Math.min(LEVEL_2, CURRENT_USDT.intValue());
            } else if (LAST_LEVEL.equals(LEVEL_2)) {
                USDT_IN_TRADE = Math.min(LEVEL_3, CURRENT_USDT.intValue());
            } else if (LAST_LEVEL.equals(LEVEL_3)) {
                USDT_IN_TRADE = Math.min(LEVEL_4, CURRENT_USDT.intValue());
            } else if (LAST_LEVEL.equals(LEVEL_4)) {
                USDT_IN_TRADE = Math.min(LEVEL_5, CURRENT_USDT.intValue());
            } else if (LAST_LEVEL.equals(LEVEL_5)) {
                USDT_IN_TRADE = Math.min(LEVEL_6, CURRENT_USDT.intValue());
            }
        } else {
            USDT_IN_TRADE = Math.min(LEVEL_1, CURRENT_USDT.intValue());
        }
        LAST_LEVEL = USDT_IN_TRADE;
    }

    @Override
    public void writeAction(CandlestickEvent candlestickEvent, String action) {
        System.out.printf("%s:time = %s, level = %s, price = %s, total usdt = %s%n",
                action, logTime(candlestickEvent.getCloseTime()), USDT_IN_TRADE, candlestickEvent.getClose(), CURRENT_USDT);
    }

    private String logTime(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        return df.format(currentDate);
    }
}
