package com.bekn.energyp2p.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bekn.energyp2p.dto.AddGridRequest;
import com.bekn.energyp2p.dto.response.GridResponseDTO;
import com.bekn.energyp2p.models.Grid;
import com.bekn.energyp2p.service.GridService;
import com.bekn.energyp2p.service.TradingService;

@RestController
@RequestMapping("/api/grids")
public class GridController {

    @Autowired
    private GridService gridService;

    @Autowired
    private TradingService tradingService;

    @PostMapping("/add")
    public ResponseEntity<Grid> addGrid(@RequestBody AddGridRequest addGridRequest) {
        Grid grid = gridService.addGrid(addGridRequest);
        return ResponseEntity.ok(grid);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GridResponseDTO> getGridById(@PathVariable Long id) {
        GridResponseDTO grid = gridService.getGridById(id);
        return ResponseEntity.ok(grid);
    }

    @GetMapping("/automated/{id}")
    public ResponseEntity<String> getAutomatedGrid(@PathVariable Long id) {
        try {
            tradingService.startAutoMaticTransactions(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to start Auto transaction process");
        }
        return ResponseEntity.ok("Started Auto transaction process");
    }
}