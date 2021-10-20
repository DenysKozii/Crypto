package com.crypto.archive;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TradeSimulatorService1Impl implements TradeSimulatorService1 {
    private final BinanceApiRestClient restClient;
    private final Integer LIMIT = 1440;
    private String SYMBOL;

    @Override
    public List<Candlestick> fillArray() {
        List<Candlestick> candlesticks = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        for (int j = 4; j <= 10; j++) {
            for (int i = 0; i <= 31; i++) {
                String startTime = String.format("%s-0%s-2021 00:01:00", i, j);
                String endTime = String.format("%s-0%s-2021 00:00:00", i + 1, j);
                try {
                    Date dateStart = sdf.parse(startTime);
                    Date dateEnd = sdf.parse(endTime);
                    candlesticks.addAll(restClient.getCandlestickBars(SYMBOL, CandlestickInterval.ONE_MINUTE, LIMIT, dateStart.getTime(), dateEnd.getTime()));
                    startTime = String.format("%s-0%s-2021 16:41:00", i, j);
                    endTime = String.format("%s-0%s-2021 00:00:00", i + 1, j);
                    dateStart = sdf.parse(startTime);
                    dateEnd = sdf.parse(endTime);
                    candlesticks.addAll(restClient.getCandlestickBars(SYMBOL, CandlestickInterval.ONE_MINUTE, LIMIT, dateStart.getTime(), dateEnd.getTime()));
                    System.out.println(startTime);
                    TimeUnit.SECONDS.sleep(1);
                } catch (ParseException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return candlesticks;
    }

    @Override
    public void writeStatistics(String symbol) {
        try {
            SYMBOL = symbol;
            BufferedWriter writer = new BufferedWriter(new FileWriter("statistics/" + symbol));
            List<Candlestick> candlesticks = fillArray();
            for (Candlestick c : candlesticks) {
                String stringBuilder =
                        c.getOpenTime() + " " +
                                c.getOpen() + " " +
                                c.getHigh() + " " +
                                c.getLow() + " " +
                                c.getClose() + " " +
                                c.getVolume() + " " +
                                c.getCloseTime() + " " +
                                c.getQuoteAssetVolume() + " " +
                                c.getNumberOfTrades() + " " +
                                c.getTakerBuyBaseAssetVolume() + " " +
                                c.getTakerBuyQuoteAssetVolume() + " " +
                                "\n";
                writer.write(stringBuilder);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

//    @Override
//    public void simulate(String symbol) {
//        WaveDto timer = new WaveDto();
//        timer.setSymbol(symbol);
//        List<Candlestick> candlesticks = readStatistics(symbol);
//        for (int i = 0; i < candlesticks.size() - CANDLES_IN_CHANNEL; i++) {
//            trade(candlesticks.subList(i, i + CANDLES_IN_CHANNEL), timer);
//        }
//        System.out.printf("total traded usdt = %s%n",
//                usdt.get() + amount.get() * Double.parseDouble(candlesticks.get(candlesticks.size() - 1).getClose()));
//        System.out.printf(" usdt passive= %s%n",
//                10000 * (Double.valueOf(candlesticks.get(candlesticks.size() - 1).getClose()) / Double.valueOf(candlesticks.get(0).getClose())));
//    }

    @Override
    public String logTime(long milliseconds) {
        Date currentDate = new Date(milliseconds);
        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
        return df.format(currentDate);
    }
}
