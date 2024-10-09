package com.bekn.energyp2p.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bekn.energyp2p.dto.BuyingRequest;
import com.bekn.energyp2p.dto.SellingRequest;
import com.bekn.energyp2p.dto.TransactionDTO;
import com.bekn.energyp2p.dto.response.BuyResponseDTO;
import com.bekn.energyp2p.dto.response.SellResponseDTO;
import com.bekn.energyp2p.dto.response.TransactionResponseDTO;
import com.bekn.energyp2p.dto.response.UserResponseDTO;
import com.bekn.energyp2p.models.BuyRequest;
import com.bekn.energyp2p.models.EnergyTransaction;
import com.bekn.energyp2p.models.SellRequest;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.service.TradingService;
import com.bekn.energyp2p.service.UserService;

@RestController
@RequestMapping("/api/trade/")
public class TradeController {
    @Autowired
    private TradingService tradingService;

    @Autowired
    private UserService userService;

    @PostMapping("/buy")
    public ResponseEntity<?> placeBuyRequest(@RequestBody BuyingRequest buyingRequest) {
        try {
            TransactionResponseDTO transactionResponseDTO = tradingService.placeBuyRequest(buyingRequest);
            if (transactionResponseDTO != null) {
                return ResponseEntity.status(200).body(transactionResponseDTO);
            } else {
                return ResponseEntity.ok("Order Placed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to place buy request: " + e.getMessage());
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<?> placeSellRequest(@RequestBody SellingRequest sellingRequest) {
        try {
            TransactionResponseDTO transactionResponseDTO = tradingService.placeSellRequest(sellingRequest);
            if (transactionResponseDTO != null) {
                return ResponseEntity.status(200).body(transactionResponseDTO);
            } else {
                return ResponseEntity.ok("Order Placed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to place sell request: " + e.getMessage());
        }
    }

    @GetMapping("/endOfDay")
    public ResponseEntity<String> processEndOfDay() {
        try {
            tradingService.processEndOfDay();
            return ResponseEntity.ok("End of day processing completed.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to complete end of day process: " + e.getMessage());
        }
    }

    @GetMapping("/buy/{id}")
    public ResponseEntity<BuyResponseDTO> getBuyStatus(@PathVariable Long id) {
        BuyResponseDTO buyRequest = tradingService.getBuyRequest(id);
        return ResponseEntity.ok(buyRequest);
    }

    @GetMapping("/buy/all")
    public ResponseEntity<List<BuyResponseDTO>> getAllBuyRequests() {
        List<BuyResponseDTO> buyRequests = tradingService.getAllBuyRequests();
        return ResponseEntity.ok(buyRequests);
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transactionResponseDTO = tradingService.getTransaction(id);
        return ResponseEntity.ok(transactionResponseDTO);
    }

    @GetMapping("/transaction/grid/{id}")
    public ResponseEntity<List<TransactionDTO>> getTransactionByGridId(@PathVariable Long id) {
        List<TransactionDTO> transactionResponseDTO = tradingService.getTransactionsByGridId(id);
        return ResponseEntity.ok(transactionResponseDTO);
    }

    @GetMapping("/sell/{id}")
    public ResponseEntity<SellResponseDTO> getSellStatus(@PathVariable Long id) {
        SellResponseDTO sellRequest = tradingService.getSellRequest(id);
        return ResponseEntity.ok(sellRequest);
    }

    @GetMapping("/sell/all")
    public ResponseEntity<List<SellResponseDTO>> getAllSellRequests() {
        List<SellResponseDTO> sellRequests = tradingService.getAllSellRequests();
        return ResponseEntity.ok(sellRequests);
    }

}
