package com.bekn.energyp2p.models;

import com.bekn.energyp2p.enums.GridStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private Integer pincode;
    private GridStatus status;
    private Double buyPricePerUnit;
    private Double buyPricePerUnitNR;
    private String reference;
    private Double sellPricePerUnit;
    private Integer capacity;
    private Integer remainingCapacity;

    @OneToOne
    private User adminUser;

}
