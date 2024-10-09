package com.bekn.energyp2p.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.bekn.energyp2p.dto.HourlyEnergyStatsDTO;
import com.bekn.energyp2p.dto.response.DailyDemandDTO;
import com.bekn.energyp2p.dto.response.DailyEnergyTradeDTO;
import com.bekn.energyp2p.dto.response.DailyGenerationDTO;
import com.bekn.energyp2p.models.BuyRequest;
import com.bekn.energyp2p.models.EnergyTransaction;
import com.bekn.energyp2p.models.SellRequest;
import com.bekn.energyp2p.repository.BuyRequestRepository;
import com.bekn.energyp2p.repository.EnergyTransactionRepository;
import com.bekn.energyp2p.repository.SellRequestRepository;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AnalyticsService {

    @Autowired
    private BuyRequestRepository buyRequestRepository;

    @Autowired
    private SellRequestRepository sellRequestRepository;

    @Autowired
    private EnergyTransactionRepository energyTransactionRepository;

    public List<DailyEnergyTradeDTO> getDailyEnergyTradeDataByGrid(Long gridId) {
        return energyTransactionRepository.getDailyEnergyTradeDataByGrid(gridId);
    }

    public List<DailyDemandDTO> getPeakDemandDataByGrid(Long gridId) {
        List<BuyRequest> buyRequests = buyRequestRepository.findAllBuyRequestsByGrid(gridId);

        Map<Date, Map<Integer, List<BuyRequest>>> hourlyData = new HashMap<>();

        for (BuyRequest request : buyRequests) {
            Date requestDate = Date.valueOf(request.getTimeStamp().toLocalDate());
            int requestHour = request.getTimeStamp().getHour();

            hourlyData.putIfAbsent(requestDate, new HashMap<>());
            Map<Integer, List<BuyRequest>> hourMap = hourlyData.get(requestDate);

            hourMap.putIfAbsent(requestHour, new ArrayList<>());
            hourMap.get(requestHour).add(request);
        }

        List<DailyDemandDTO> result = new ArrayList<>();

        for (Map.Entry<Date, Map<Integer, List<BuyRequest>>> dayEntry : hourlyData.entrySet()) {
            Date day = dayEntry.getKey();
            Map<Integer, List<BuyRequest>> hoursData = dayEntry.getValue();

            int peakHour = 0;
            long maxRequests = 0;
            int maxEnergyDemand = 0;

            for (Map.Entry<Integer, List<BuyRequest>> hourEntry : hoursData.entrySet()) {
                int hour = hourEntry.getKey();
                List<BuyRequest> requests = hourEntry.getValue();
                long totalRequests = requests.size();
                int totalEnergyUnits = requests.stream().mapToInt(BuyRequest::getEnergyUnits).sum();

                if (totalRequests > maxRequests) {
                    maxRequests = totalRequests;
                    maxEnergyDemand = totalEnergyUnits;
                    peakHour = hour;
                }
            }

            DailyDemandDTO dailyDemandDTO = DailyDemandDTO.builder()
                    .date(day)
                    .numberOfRequests(maxRequests)
                    .totalEnergyUnits((long) maxEnergyDemand)
                    .hour(peakHour)
                    .totalRequests(maxRequests)
                    .maxEnergyDemand(maxEnergyDemand)
                    .maxRequests(maxRequests)
                    .build();

            result.add(dailyDemandDTO);
        }

        return result;
    }

    // For Sell Requests Peak Generation Data
    public List<DailyGenerationDTO> getPeakGenerationDataByGrid(Long gridId) {
        List<SellRequest> sellRequests = sellRequestRepository.findAllSellRequestsByGrid(gridId);

        Map<Date, Map<Integer, Long>> hourlyEnergyData = new HashMap<>();

        for (SellRequest request : sellRequests) {
            Date requestDate = Date.valueOf(request.getTimeStamp().toLocalDate());
            int requestHour = request.getTimeStamp().getHour();
            long energyUnits = request.getEnergyUnits();

            hourlyEnergyData.putIfAbsent(requestDate, new HashMap<>());
            Map<Integer, Long> hourMap = hourlyEnergyData.get(requestDate);

            hourMap.put(requestHour, hourMap.getOrDefault(requestHour, 0L) + energyUnits);
        }

        List<DailyGenerationDTO> result = new ArrayList<>();

        for (Map.Entry<Date, Map<Integer, Long>> dayEntry : hourlyEnergyData.entrySet()) {
            Date day = dayEntry.getKey();
            Map<Integer, Long> hoursData = dayEntry.getValue();

            int peakHour = 0;
            long maxEnergyGeneration = 0;
            long totalEnergyUnits = 0;

            for (Map.Entry<Integer, Long> hourEntry : hoursData.entrySet()) {
                int hour = hourEntry.getKey();
                long energyGenerated = hourEntry.getValue();

                totalEnergyUnits += energyGenerated;

                if (energyGenerated > maxEnergyGeneration) {
                    maxEnergyGeneration = energyGenerated;
                    peakHour = hour;
                }
            }

            DailyGenerationDTO dailyGenerationDTO = DailyGenerationDTO.builder()
                    .date(day)
                    .totalEnergyUnits(totalEnergyUnits)
                    .hour(peakHour)
                    .maxEnergyGeneration(maxEnergyGeneration)
                    .build();

            result.add(dailyGenerationDTO);
        }

        return result;
    }

    public List<HourlyEnergyStatsDTO> getEnergyReport(Long gridId, LocalDate date, int interval) {
        List<HourlyEnergyStatsDTO> hourlyEnergyStatsDTOs = new ArrayList<>();

        LocalDateTime startOfDay = date.atStartOfDay();

        for (int hour = 0; hour < 24; hour += interval) {
            LocalDateTime intervalStart = startOfDay.plusHours(hour);
            LocalDateTime intervalEnd = startOfDay.plusHours(hour + interval);

            Long energyTraded = energyTransactionRepository.sumTransactionAmount(gridId, intervalStart, intervalEnd);
            Long energyDemand = buyRequestRepository.sumEnergyUnitsByGridAndDate(gridId, intervalStart, intervalEnd);
            Long energyProduced = sellRequestRepository.sumEnergyUnitsByGridAndDate(gridId, intervalStart, intervalEnd);

            HourlyEnergyStatsDTO reportEntry = new HourlyEnergyStatsDTO();
            reportEntry.setHour(hour);
            reportEntry.setEnergyTraded(energyTraded);
            reportEntry.setEnergyDemand(energyDemand);
            reportEntry.setEnergyProduced(energyProduced);

            hourlyEnergyStatsDTOs.add(reportEntry);
        }

        return hourlyEnergyStatsDTOs;
    }
}