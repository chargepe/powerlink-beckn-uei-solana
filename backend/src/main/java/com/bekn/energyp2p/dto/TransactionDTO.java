package com.bekn.energyp2p.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.bekn.energyp2p.enums.TransactionStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private Integer transactionAmount;
    private String transactionSignature;
    private TransactionStatus transactionStatus;
    private Double price;
    private LocalDateTime date;
}
