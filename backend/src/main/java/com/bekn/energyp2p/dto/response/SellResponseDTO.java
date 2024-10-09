package com.bekn.energyp2p.dto.response;

import java.time.LocalDateTime;
import com.bekn.energyp2p.enums.RequestStatus;
import com.bekn.energyp2p.models.SellRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellResponseDTO {

    private Long sellerId;
    private Integer sellerEnergyConsumed;
    private Double price;
    private LocalDateTime timestamp;
    private RequestStatus status;
    private Long transactionId;
    private String transactionSignature;

    public static SellResponseDTO fromSellRequest(SellRequest sellRequest) {
        return SellResponseDTO.builder()
                .sellerId(sellRequest.getSeller().getId())
                .sellerEnergyConsumed(sellRequest.getEnergyUnits())
                .price(sellRequest.getPricePerUnit())
                .transactionId(sellRequest.getTransactionId())
                .transactionSignature(sellRequest.getTransactionSignature())
                .timestamp(sellRequest.getTimeStamp())
                .status(sellRequest.getStatus())
                .build();
    }
}
