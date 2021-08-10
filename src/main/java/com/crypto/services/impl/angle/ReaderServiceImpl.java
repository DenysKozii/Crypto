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
    private String passiveUSDTTime = "0.0";
    private String status = "WAIT";

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
                infoDto.setUsdt(line[3]);
                infoDto.setUsdtPassive(line[4]);
                infoDto.setStatus(line[5]);
                infoDto.setDate(line[6]);
                passiveUSDT = infoDto.getUsdtPassive();
                USDT = infoDto.getUsdt();
                status = infoDto.getStatus();
                if (passiveUSDTTime.equals("0.0"))
                    passiveUSDTTime = infoDto.getTime();
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