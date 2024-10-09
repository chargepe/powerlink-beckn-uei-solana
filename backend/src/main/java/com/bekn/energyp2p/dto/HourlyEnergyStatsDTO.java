package com.bekn.energyp2p.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HourlyEnergyStatsDTO {
    private Integer hour;
    private Long energyTraded;
    private Long energyDemand;
    private Long energyProduced;
}