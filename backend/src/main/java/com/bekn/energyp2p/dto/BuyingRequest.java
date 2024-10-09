package com.bekn.energyp2p.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyingRequest {
    private Long buyerId;
    private Long sellRequestId;
    private Integer energyUnits;
    private Double maxPricePerUnit;
}