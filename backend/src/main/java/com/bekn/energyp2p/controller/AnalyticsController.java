package com.bekn.energyp2p.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bekn.energyp2p.dto.HourlyEnergyStatsDTO;
import com.bekn.energyp2p.dto.response.DailyDemandDTO;
import com.bekn.energyp2p.dto.response.DailyEnergyTradeDTO;
import com.bekn.energyp2p.dto.response.DailyGenerationDTO;
import com.bekn.energyp2p.service.AnalyticsService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/tradedEnergy/{gridId}")
    public List<DailyEnergyTradeDTO> getDailyEnergyTradeByGrid(@PathVariable Long gridId) {
        return analyticsService.getDailyEnergyTradeDataByGrid(gridId);
    }

    @GetMapping("/peakDemand/{gridId}")
    public List<DailyDemandDTO> getPeakDemandByGrid(@PathVariable Long gridId) {
        return analyticsService.getPeakDemandDataByGrid(gridId);
    }

    @GetMapping("/peakGeneration/{gridId}")
    public List<DailyGenerationDTO> getPeakGenerationByGrid(@PathVariable Long gridId) {
        return analyticsService.getPeakGenerationDataByGrid(gridId);
    }

    @GetMapping("/energyReport")
    public List<HourlyEnergyStatsDTO> getEnergyReport(
            @RequestParam Long gridId,
            @RequestParam String date,
            @RequestParam int interval) {

        LocalDate reportDate = LocalDate.parse(date);
        return analyticsService.getEnergyReport(gridId, reportDate, interval);
    }
}
