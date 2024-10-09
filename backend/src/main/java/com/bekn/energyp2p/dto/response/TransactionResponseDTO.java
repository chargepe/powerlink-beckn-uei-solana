package com.bekn.energyp2p.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Long transactionId;
    private Long buyerId;
    private Integer buyerEnergyConsumed;
    private Long sellerId;
    private String transactionSignature;
    private Integer transactionAmount;
    private Double price;
    private LocalDateTime timestamp;
    private String status;
}