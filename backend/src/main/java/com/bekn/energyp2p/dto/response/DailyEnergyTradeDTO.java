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
public class DailyEnergyTradeDTO {
    private Date date;
    private Long totalEnergyTraded;
    private Long totalTransactions;
    private Double totalPrice;
    private Double avgPricePerUnit;
    private Long gridId;
}
