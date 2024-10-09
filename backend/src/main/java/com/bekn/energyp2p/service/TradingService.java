package com.bekn.energyp2p.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bekn.energyp2p.dto.BuyingRequest;
import com.bekn.energyp2p.dto.SellingRequest;
import com.bekn.energyp2p.dto.TransactionDTO;
import com.bekn.energyp2p.dto.response.BuyResponseDTO;
import com.bekn.energyp2p.dto.response.SellResponseDTO;
import com.bekn.energyp2p.dto.response.TransactionResponseDTO;
import com.bekn.energyp2p.enums.GridStatus;
import com.bekn.energyp2p.enums.RequestStatus;
import com.bekn.energyp2p.enums.TransactionStatus;
import com.bekn.energyp2p.models.BuyRequest;
import com.bekn.energyp2p.models.EnergyTransaction;
import com.bekn.energyp2p.models.Grid;
import com.bekn.energyp2p.models.SellRequest;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.models.Wallet;
import com.bekn.energyp2p.repository.BuyRequestRepository;
import com.bekn.energyp2p.repository.EnergyTransactionRepository;
import com.bekn.energyp2p.repository.GridRepository;
import com.bekn.energyp2p.repository.SellRequestRepository;
import com.bekn.energyp2p.repository.UserRepository;
import com.bekn.energyp2p.util.BlockchainUtility;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradingService {

    @Autowired
    private BuyRequestRepository buyRequestRepository;

    @Autowired
    private SellRequestRepository sellRequestRepository;

    @Autowired
    private EnergyTransactionRepository energyTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private BlockchainUtility blockchainUtility;

    @Value("${grid.noOfAutomaticTransactions}")
    private Integer noOfAutomaticTransactions;

    @Transactional
    public TransactionResponseDTO placeBuyRequest(BuyingRequest buyingRequest) {
        User buyer = userRepository.findById(buyingRequest.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Integer energyUnits = buyingRequest.getEnergyUnits();
        Double maxPricePerUnit = buyingRequest.getMaxPricePerUnit();

        BuyRequest buyRequest = BuyRequest.builder()
                .buyer(buyer)
                .energyUnits(energyUnits)
                .maxPricePerUnit(maxPricePerUnit)
                .timeStamp(LocalDateTime.now())
                .status(RequestStatus.ORDER_PLACED)
                .build();
        buyRequestRepository.save(buyRequest);

        List<SellRequest> matchingSellRequests = sellRequestRepository.findSellRequestsByPriceAndUnits(maxPricePerUnit,
                energyUnits);

        for (SellRequest sellRequest : matchingSellRequests) {
            if (sellRequest.getPricePerUnit() <= maxPricePerUnit && sellRequest.getRemainingUnits() >= energyUnits) {
                EnergyTransaction energyTransaction = executeTransaction(sellRequest, buyRequest);

                return TransactionResponseDTO.builder()
                        .transactionId(energyTransaction.getId())
                        .buyerId(buyer.getId())
                        .buyerEnergyConsumed(buyer.getMeter().getEnergyConsumed())
                        .sellerId(sellRequest.getSeller().getId())
                        .transactionAmount(energyTransaction.getTransactionAmount())
                        .transactionSignature(energyTransaction.getTransactionSignature())
                        .price(energyTransaction.getPrice())
                        .timestamp(energyTransaction.getTimestamp())
                        .status(energyTransaction.getStatus().name())
                        .build();
            }
        }

        return null;
    }

    @Transactional
    public TransactionResponseDTO placeSellRequest(SellingRequest sellingRequest) {
        User seller = userRepository.findById(sellingRequest.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Integer energyUnits = sellingRequest.getEnergyUnits();
        Double pricePerUnit = sellingRequest.getPricePerUnit();

        SellRequest sellRequest = SellRequest.builder()
                .seller(seller)
                .energyUnits(energyUnits)
                .pricePerUnit(pricePerUnit)
                .remainingUnits(energyUnits)
                .timeStamp(LocalDateTime.now())
                .status(RequestStatus.ORDER_PLACED)
                .build();
        sellRequestRepository.save(sellRequest);

        List<BuyRequest> matchingBuyRequests = buyRequestRepository.findBuyRequestsByPriceAndUnits(pricePerUnit,
                energyUnits);

        for (BuyRequest buyRequest : matchingBuyRequests) {
            if (buyRequest.getEnergyUnits() <= sellRequest.getRemainingUnits()) {
                EnergyTransaction energyTransaction = executeTransaction(sellRequest, buyRequest);

                return TransactionResponseDTO.builder()
                        .transactionId(energyTransaction.getId())
                        .buyerId(buyRequest.getBuyer().getId())
                        .sellerId(seller.getId())
                        .transactionAmount(energyTransaction.getTransactionAmount())
                        .transactionSignature(energyTransaction.getTransactionSignature())
                        .price(energyTransaction.getPrice())
                        .timestamp(energyTransaction.getTimestamp())
                        .status(energyTransaction.getStatus().name())
                        .build();
            }
        }

        return null;
    }

    public EnergyTransaction executeTransaction(SellRequest sellRequest, BuyRequest buyRequest) {
        try {
            log.info("Executing transaction between buyer: {} and seller: {}", buyRequest.getBuyer().getId(),
                    sellRequest.getSeller().getId());

            double totalCost = buyRequest.getEnergyUnits() * sellRequest.getPricePerUnit();
            long totalCostInLamports = blockchainUtility.convertRupeesToLamports(totalCost);

            if (buyRequest.getBuyer().getWallet().getBalance() < totalCost) {
                log.error("Insufficient funds in buyer's wallet: {}", buyRequest.getBuyer().getId());
                throw new RuntimeException("Insufficient funds in buyer's wallet.");
            }

            if (sellRequest.getRemainingUnits() < buyRequest.getEnergyUnits()) {
                log.error("Not enough units left in sell request: {}", sellRequest.getId());
                throw new RuntimeException("Not enough units left in sell request.");
            }

            log.info("Transferring {} lamports from buyer to seller", totalCostInLamports);
            Wallet buyerWallet = buyRequest.getBuyer().getWallet();
            Wallet sellerWallet = sellRequest.getSeller().getWallet();

            String transactionSignature = blockchainUtility.transfer(buyerWallet.getSecretKey(),
                    sellerWallet.getPublicKey(), totalCostInLamports);

            EnergyTransaction transaction = EnergyTransaction.builder()
                    .buyer(buyRequest.getBuyer())
                    .seller(sellRequest.getSeller())
                    .transactionAmount(buyRequest.getEnergyUnits())
                    .price(totalCost)
                    .timestamp(LocalDateTime.now())
                    .status(TransactionStatus.SUCCESSFUL)
                    .transactionSignature(transactionSignature)
                    .grid(sellRequest.getSeller().getGrid())
                    .build();

            log.info("Updating balances and meters");
            updateBalancesAndMeters(sellRequest, buyRequest, totalCost);

            EnergyTransaction energyTransaction = energyTransactionRepository.save(transaction);

            Integer remainingUnits = sellRequest.getRemainingUnits() - buyRequest.getEnergyUnits();
            sellRequest.setRemainingUnits(remainingUnits);

            if (remainingUnits == 0) {
                sellRequest.setTransactionSignature(transactionSignature);
                sellRequest.setTransactionId(energyTransaction.getId());
                sellRequest.setStatus(RequestStatus.COMPLETE);
            }
            buyRequest.setTransactionSignature(transactionSignature);
            buyRequest.setTransactionId(energyTransaction.getId());
            buyRequest.setStatus(RequestStatus.COMPLETE);

            sellRequestRepository.save(sellRequest);
            buyRequestRepository.save(buyRequest);

            log.info("Transaction successful");
            return transaction;
        } catch (Exception e) {
            log.error("Transaction failed: {}", e.getMessage());
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

    private void updateBalancesAndMeters(SellRequest sellRequest, BuyRequest buyRequest, double totalCost) {
        User seller = sellRequest.getSeller();
        User buyer = buyRequest.getBuyer();

        // Update seller's balance and energy generated
        seller.getWallet().setBalance(seller.getWallet().getBalance() + totalCost);
        seller.getMeter().setEnergyGenerated(seller.getMeter().getEnergyGenerated() + buyRequest.getEnergyUnits());

        // Update buyer's balance and energy consumed
        buyer.getWallet().setBalance(buyer.getWallet().getBalance() - totalCost);
        buyer.getMeter().setEnergyConsumed(buyer.getMeter().getEnergyConsumed() + buyRequest.getEnergyUnits());

        userRepository.save(buyer);
        userRepository.save(seller);
    }

    @Transactional
    public void processEndOfDay() {
        Grid grid = gridRepository.findByStatus(GridStatus.LIVE)
                .orElseThrow(() -> new RuntimeException("Live grid not found"));

        Double gridBuyPricePerUnit = grid.getBuyPricePerUnit();
        Integer remainingGridCapacity = grid.getRemainingCapacity();

        List<SellRequest> openSellRequests = sellRequestRepository.findAllByStatus(RequestStatus.ORDER_PLACED);

        int totalSellUnits = openSellRequests.stream()
                .mapToInt(SellRequest::getRemainingUnits)
                .sum();

        if (totalSellUnits > remainingGridCapacity) {
            throw new RuntimeException("Grid does not have enough capacity to buy all remaining energy.");
        }

        for (SellRequest sellRequest : openSellRequests) {
            Integer unitsToSell = sellRequest.getRemainingUnits();
            User seller = sellRequest.getSeller();

            double totalCost = unitsToSell * gridBuyPricePerUnit;
            seller.getWallet().setBalance(seller.getWallet().getBalance() + totalCost);
            seller.getMeter().setEnergyGenerated(seller.getMeter().getEnergyGenerated() + unitsToSell);

            sellRequest.setStatus(RequestStatus.COMPLETE);
            sellRequest.setRemainingUnits(0);

            userRepository.save(seller);
            sellRequestRepository.save(sellRequest);

            grid.setRemainingCapacity(grid.getRemainingCapacity() - unitsToSell);
        }

        List<BuyRequest> openBuyRequests = buyRequestRepository.findAllByStatus(RequestStatus.ORDER_PLACED);

        for (BuyRequest buyRequest : openBuyRequests) {
            buyRequest.setStatus(RequestStatus.REJECTED);
            buyRequestRepository.save(buyRequest);
        }

        gridRepository.save(grid);
    }

    public BuyResponseDTO getBuyRequest(Long id) {
        BuyRequest buyRequest = buyRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Buy request not found"));

        return BuyResponseDTO.fromBuyRequest(buyRequest);

    }

    public SellResponseDTO getSellRequest(Long id) {
        SellRequest sellRequest = sellRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("sell request not found"));

        return SellResponseDTO.fromSellRequest(sellRequest);
    }

    public TransactionDTO getTransaction(Long id) {
        EnergyTransaction transaction = energyTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        return TransactionDTO.builder()
                .transactionAmount(transaction.getTransactionAmount())
                .transactionSignature(transaction.getTransactionSignature())
                .transactionStatus(transaction.getStatus())
                .price(transaction.getPrice())
                .date(transaction.getTimestamp())
                .build();
    }

    public List<TransactionDTO> getTransactionsByGridId(Long id) {
        List<EnergyTransaction> transactions = energyTransactionRepository.findAllByGridId(id);

        return transactions.stream()
                .map(transaction -> TransactionDTO.builder()
                        .transactionAmount(transaction.getTransactionAmount())
                        .transactionSignature(transaction.getTransactionSignature())
                        .transactionStatus(transaction.getStatus())
                        .price(transaction.getPrice())
                        .date(transaction.getTimestamp())
                        .build())
                .toList();
    }

    public List<SellResponseDTO> getAllSellRequests() {

        List<SellRequest> sellRequests = sellRequestRepository.findAll();

        return sellRequests.stream()
                .map(SellResponseDTO::fromSellRequest)
                .toList();
    }

    public List<BuyResponseDTO> getAllBuyRequests() {

        List<BuyRequest> buyRequests = buyRequestRepository.findAll();

        return buyRequests.stream()
                .map(BuyResponseDTO::fromBuyRequest)
                .toList();
    }

    @Transactional
    public void startAutoMaticTransactions(Long gridId) throws InterruptedException {
        Integer totalTransactions = noOfAutomaticTransactions;
        int transactionCount = 0;
        Random random = new Random();

        List<User> gridUsers = userRepository.findAll().stream()
                .filter(user -> user.getGrid() != null && user.getGrid().getId().equals(gridId))
                .collect(Collectors.toList());

        if (gridUsers.isEmpty()) {
            log.info("No users found for gridId: {}", gridId);
            return;
        }

        while (transactionCount < totalTransactions) {
            User user = gridUsers.get(random.nextInt(gridUsers.size()));

            boolean isBuying = random.nextBoolean();

            if (isBuying) {
                simulateBuyRequest(user);
            } else {
                simulateSellRequest(user);
            }

            transactionCount++;
            Thread.sleep(1);
        }
        log.info("Completed {} transactions for gridId: {}", transactionCount, gridId);
    }

    private void simulateBuyRequest(User buyer) {
        Random random = new Random();

        int energyUnits = random.nextInt(5) + 1;
        double maxPricePerUnit = random.nextDouble() * 5;

        BuyingRequest buyingRequest = BuyingRequest.builder()
                .buyerId(buyer.getId())
                .maxPricePerUnit(maxPricePerUnit)
                .energyUnits(energyUnits)
                .build();

        log.info("Simulating buy request from user {} with {} units at max price {}", buyer.getId(), energyUnits,
                maxPricePerUnit);
        TransactionResponseDTO transactionResponseDTO = placeBuyRequest(buyingRequest);
        if (transactionResponseDTO != null) {
            log.info("Buy request successful: {}", transactionResponseDTO);
        } else {
            log.info("Buy request failed");
        }
    }

    private void simulateSellRequest(User seller) {
        Random random = new Random();

        int energyUnits = random.nextInt(5) + 1;
        double pricePerUnit = random.nextDouble() * 5;

        SellingRequest sellingRequest = SellingRequest.builder()
                .sellerId(seller.getId())
                .energyUnits(energyUnits)
                .pricePerUnit(pricePerUnit)
                .build();

        log.info("Simulating sell request from user {} with {} units at price {}", seller.getId(), energyUnits,
                pricePerUnit);
        TransactionResponseDTO transactionResponseDTO = placeSellRequest(sellingRequest);
        if (transactionResponseDTO != null) {
            log.info("Sell request successful: {}", transactionResponseDTO);
        } else {
            log.info("Sell request failed");
        }
    }
}