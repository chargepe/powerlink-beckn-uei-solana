package com.bekn.energyp2p.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddGridRequest {
    private String location;
    private Integer pincode;
    private Long adminUserId;
}