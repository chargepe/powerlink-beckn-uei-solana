package com.bekn.energyp2p.dto;

import com.bekn.energyp2p.enums.EnergyExchangeCapabilityType;
import com.bekn.energyp2p.enums.IndustryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {

    private String name;
    private String address;
    private String email;
    private EnergyExchangeCapabilityType energyExchangeCapability;
    private IndustryType industryType;
    private Double initialBalance;
    private Long gridId;
}
