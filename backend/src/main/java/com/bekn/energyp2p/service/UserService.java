package com.bekn.energyp2p.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bekn.energyp2p.constants.ParentPaymentWallet;
import com.bekn.energyp2p.dto.UserRegistrationRequest;
import com.bekn.energyp2p.dto.WalletDTO;
import com.bekn.energyp2p.dto.response.RegisterUserResponseDTO;
import com.bekn.energyp2p.dto.response.UserResponseDTO;
import com.bekn.energyp2p.models.Grid;
import com.bekn.energyp2p.models.Meter;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.models.Wallet;
import com.bekn.energyp2p.repository.GridRepository;
import com.bekn.energyp2p.repository.UserRepository;
import com.bekn.energyp2p.util.BlockchainUtility;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;

@Service
public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private GridRepository gridRepository;

        @Autowired
        private BlockchainUtility blockchainUtility;

        @Value("${user.initialBalance}")
        private Double initialUserBalance;
        @Value("${user.lamportPerRupeee}")
        private Integer lamportPerRupeee;

        public UserResponseDTO getUser(Long id) {
                User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
                UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
                return userResponseDTO;

        }

        public UserResponseDTO checkIfUserExsists(String email) {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
                UserResponseDTO userResponseDTO = UserResponseDTO.fromUser(user);
                return userResponseDTO;

        }

        public JsonNode getTransactionDetails(String transactionString) {
                JsonNode json = null;
                try {
                        json = blockchainUtility.getTransactionDetails(transactionString);
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                return json;
        }

        public RegisterUserResponseDTO registerUser(UserRegistrationRequest userRegistrationRequest) {
                User existingUser = userRepository.findByEmail(userRegistrationRequest.getEmail())
                                .orElse(null);
                if (existingUser != null) {
                        RegisterUserResponseDTO registerUserResponseDTO = RegisterUserResponseDTO
                                        .fromUser(existingUser);
                        return registerUserResponseDTO;
                }

                Grid grid = null;
                if (userRegistrationRequest.getGridId() != null) {
                        grid = gridRepository.findById(userRegistrationRequest.getGridId())
                                        .orElseThrow(() -> new RuntimeException("Grid not found"));
                }

                User user = User.builder()
                                .name(userRegistrationRequest.getName())
                                .email(userRegistrationRequest.getEmail())
                                .address(userRegistrationRequest.getAddress())
                                .energyExchangeCapability(userRegistrationRequest.getEnergyExchangeCapability())
                                .industryType(userRegistrationRequest.getIndustryType())
                                .grid(grid)
                                .build();

                WalletDTO blockchainWallet = blockchainUtility.createWallet();

                Wallet wallet = Wallet.builder()
                                .balance(initialUserBalance)
                                .publicKey(blockchainWallet.getPublicKey())
                                .secretKey(blockchainWallet.getSecretKey())
                                .solPerRupee(blockchainUtility.convertLamportsToSol(lamportPerRupeee))
                                .build();

                user.setWallet(wallet);

                Meter meter = Meter.builder()
                                .energyGenerated(0)
                                .energyConsumed(0)
                                .lastReading(null)
                                .user(user)
                                .build();
                user.setMeter(meter);

                User newUser = userRepository.save(user);

                try {

                        String transactionSignature = blockchainUtility.fundUserWallet(blockchainWallet.getPublicKey(),
                                        initialUserBalance);
                        newUser.setInitialTransactionString(transactionSignature);

                        userRepository.save(newUser);
                        System.out.println(
                                        "Initial transfer successful. Transaction Signature: " + transactionSignature);
                } catch (Exception e) {
                        System.out.println("Initial transfer failed: " + e.getMessage());
                        throw new RuntimeException("Initial transfer failed: " + e.getMessage());
                }

                // Return the response with user information
                RegisterUserResponseDTO registerUserResponseDTO = RegisterUserResponseDTO.fromUser(newUser);
                return registerUserResponseDTO;
        }

}
