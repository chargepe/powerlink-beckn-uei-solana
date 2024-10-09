package com.bekn.energyp2p.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "grid")
@Data
public class GridConstants {

    private String location;
    private int pincode;
    private int capacity;
    private double buyPricePerUnit;
    private double sellPricePerUnit;
    private double buyPricePerUnitNR;
    private String reference;
    private String status;

}
