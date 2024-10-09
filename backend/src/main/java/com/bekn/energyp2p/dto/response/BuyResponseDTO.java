package com.bekn.energyp2p.dto.response;

import java.time.LocalDateTime;

import com.bekn.energyp2p.enums.RequestStatus;
import com.bekn.energyp2p.models.BuyRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyResponseDTO {

    private Long buyerId;
    private Integer buyerEnergyConsumed;
    private LocalDateTime timestamp;
    private RequestStatus status;
    private Long transactionId;
    private String transactionSignature;

    public static BuyResponseDTO fromBuyRequest(BuyRequest buyRequest) {
        return BuyResponseDTO.builder()
                .buyerId(buyRequest.getBuyer().getId())
                .buyerEnergyConsumed(buyRequest.getEnergyUnits())
                .transactionId(buyRequest.getTransactionId())
                .transactionSignature(buyRequest.getTransactionSignature())
                .timestamp(buyRequest.getTimeStamp())
                .status(buyRequest.getStatus())
                .build();
    }
}
