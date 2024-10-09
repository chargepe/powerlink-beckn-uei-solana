package com.bekn.energyp2p.dto.response;

import com.bekn.energyp2p.enums.GridStatus;
import com.bekn.energyp2p.models.Grid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GridResponseDTO {
    private Long id;
    private String location;
    private Integer pincode;
    private GridStatus status;
    private Double buyPricePerUnit;
    private Double sellPricePerUnit;
    private Double buyPricePerUnitNR;
    private String reference;
    private Integer capacity;
    private Integer remainingCapacity;
    private Long adminUserId;

    public static GridResponseDTO fromGrid(Grid grid) {
        return GridResponseDTO.builder()
                .buyPricePerUnit(grid.getBuyPricePerUnit())
                .id(grid.getId())
                .location(grid.getLocation())
                .pincode(grid.getPincode())
                .status(grid.getStatus())
                .sellPricePerUnit(grid.getSellPricePerUnit())
                .buyPricePerUnitNR(grid.getBuyPricePerUnitNR())
                .reference(grid.getReference())
                .capacity(grid.getCapacity())
                .remainingCapacity(grid.getRemainingCapacity())
                .adminUserId(grid.getAdminUser().getId())
                .build();
    }
}
