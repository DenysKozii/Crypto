package com.crypto.services.impl.angle;

import com.binance.api.client.domain.market.Candlestick;
import com.crypto.dto.InfoDto;
import com.crypto.services.ReaderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Getter
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private String passiveUSDT;
    private String USDT;
    private String low = "0.0";
    private String lowTime = "0.0";
    private String high = "0.0";
    private String highTime = "0.0";
    private String passiveUSDTTime = "0.0";

    @Override
    public List<InfoDto> readStatistics() {
        List<InfoDto> infos = new ArrayList<>();
        try {
            File file = new File("trading/DOGEUSDT");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String[] line = myReader.nextLine().split(" ");
                InfoDto infoDto = new InfoDto();
                infoDto.setTime(line[0]);
                infoDto.setAction(line[1]);
                infoDto.setPrice(line[2]);
                infoDto.setLow(line[3]);
                infoDto.setHigh(line[4]);
                infoDto.setUsdt(line[5]);
                infoDto.setUsdtPassive(line[6]);
                infoDto.setDate(line[7]);
                passiveUSDT = infoDto.getUsdtPassive();
                USDT = infoDto.getUsdt();
                if (!low.equals(infoDto.getLow()))
                    lowTime = infoDto.getTime();
                if (!high.equals(infoDto.getHigh()))
                    highTime = infoDto.getTime();
                if (passiveUSDTTime.equals("0.0"))
                    passiveUSDTTime = infoDto.getTime();
                low = infoDto.getLow();
                high = infoDto.getHigh();
                if (!"WAIT".equals(infoDto.getAction()))
                    infos.add(infoDto);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return infos;
    }
}