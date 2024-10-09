package com.bekn.energyp2p.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellingRequest {
    private Long sellerId;
    private Integer energyUnits;
    private Double pricePerUnit;
}
