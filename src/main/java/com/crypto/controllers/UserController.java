package com.crypto.controllers;

import com.crypto.dto.InfoDto;
import com.crypto.services.ReaderService;
import com.crypto.services.impl.angle.ReaderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@AllArgsConstructor
public class UserController {

    private final ReaderServiceImpl readerService;

    @GetMapping
    public String infos(Model model) {
        List<InfoDto> infos = readerService.readStatistics();
        model.addAttribute("infos", infos);
        model.addAttribute("USDT", readerService.getUSDT());
        model.addAttribute("passiveUSDT", readerService.getPassiveUSDT());
        model.addAttribute("low", readerService.getLow());
        model.addAttribute("lowTime", readerService.getLowTime());
        model.addAttribute("high", readerService.getHigh());
        model.addAttribute("highTime", readerService.getHighTime());
        model.addAttribute("passiveUSDTTime", readerService.getPassiveUSDTTime());
        return "info";
    }
}
