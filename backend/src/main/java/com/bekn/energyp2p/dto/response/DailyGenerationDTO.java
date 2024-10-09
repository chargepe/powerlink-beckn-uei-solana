package com.bekn.energyp2p.dto.response;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyGenerationDTO {
    private Date date;
    private Long numberOfRequests;
    private Long totalEnergyUnits;
    private Double averagePricePerUnit;
    private Integer hour;
    private Long maxEnergyGeneration;
    private Long maxRequests;
}