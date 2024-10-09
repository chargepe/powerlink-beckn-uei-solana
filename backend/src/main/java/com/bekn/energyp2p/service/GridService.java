package com.bekn.energyp2p.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bekn.energyp2p.constants.GridConstants;
import com.bekn.energyp2p.dto.AddGridRequest;
import com.bekn.energyp2p.dto.response.GridResponseDTO;
import com.bekn.energyp2p.enums.GridStatus;
import com.bekn.energyp2p.models.Grid;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.repository.GridRepository;
import com.bekn.energyp2p.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class GridService {

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridConstants gridConstants;

    public GridResponseDTO getGridById(Long gridId) {
        Grid grid = gridRepository.findById(gridId)
                .orElseThrow(() -> new RuntimeException("Grid not found with id: " + gridId));

        return GridResponseDTO.fromGrid(grid);

    }

    @Transactional
    public Grid addGrid(AddGridRequest addGridRequest) {
        User adminUser = userRepository.findById(addGridRequest.getAdminUserId())
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        Grid grid = Grid.builder()
                .location(addGridRequest.getLocation())
                .pincode(addGridRequest.getPincode())
                .buyPricePerUnit(gridConstants.getBuyPricePerUnit())
                .sellPricePerUnit(gridConstants.getSellPricePerUnit())
                .buyPricePerUnitNR(gridConstants.getBuyPricePerUnitNR())
                .reference(gridConstants.getReference())
                .capacity(gridConstants.getCapacity())
                .remainingCapacity(gridConstants.getCapacity())
                .status(GridStatus.LIVE)
                .adminUser(adminUser)
                .build();

        return gridRepository.save(grid);
    }

}