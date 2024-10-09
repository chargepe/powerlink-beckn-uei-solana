package com.bekn.energyp2p.dto.response;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyDemandDTO {
    private Date date;
    private Long numberOfRequests;
    private Long totalEnergyUnits;
    private Integer hour;
    private Long totalRequests;
    private Integer maxEnergyDemand;
    private Long maxRequests;
}
