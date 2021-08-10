package com.crypto.controllers;

import com.crypto.dto.InfoDto;
import com.crypto.services.ReaderService;
import com.crypto.services.impl.angle.ReaderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Controller
@AllArgsConstructor
public class InfoController {

    private final ReaderServiceImpl readerService;

    @GetMapping
    public String infos(Model model) {
        readerService.readStatistics();
        model.addAttribute("USDT", readerService.getUSDT());
        model.addAttribute("passiveUSDT", readerService.getPassiveUSDT());
        model.addAttribute("passiveUSDTTime", readerService.getPassiveUSDTTime());
        model.addAttribute("status", readerService.getStatus());
        return "info";
    }

    @GetMapping("/transactions")
    public String transactions(Model model) {
        List<InfoDto> infos = readerService.readStatistics();
        Collections.reverse(infos);
        model.addAttribute("infos", infos);
        return "transactions";
    }
}
