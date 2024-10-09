package com.bekn.energyp2p.models;

import java.time.LocalDateTime;

import com.bekn.energyp2p.enums.RequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private User seller;

    private Integer energyUnits;
    private Double pricePerUnit;
    private LocalDateTime timeStamp;
    private Integer remainingUnits;
    private Long transactionId;
    private String transactionSignature;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}