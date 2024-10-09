package com.bekn.energyp2p.dto.response;

import com.bekn.energyp2p.dto.TransactionDTO;
import com.bekn.energyp2p.models.EnergyTransaction;
import com.bekn.energyp2p.models.Meter;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.models.Wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
        private Long id;
        private String name;
        private String email;
        private Wallet wallet;
        private String initialTransactionString;
        private Meter meter;
        private List<TransactionDTO> buyingTransactions;
        private List<TransactionDTO> sellingTransactions;

        public static UserResponseDTO fromUser(User user) {
                List<TransactionDTO> buyingTransactions = user.getBuyingTransactions()
                                .stream()
                                .map(transaction -> TransactionDTO.builder()
                                                .transactionAmount(transaction.getTransactionAmount())
                                                .price(transaction.getPrice().doubleValue())
                                                .date(transaction.getTimestamp())
                                                .build())
                                .collect(Collectors.toList());

                List<TransactionDTO> sellingTransactions = user.getSellingTransactions()
                                .stream()
                                .map(transaction -> TransactionDTO.builder()
                                                .transactionAmount(transaction.getTransactionAmount())
                                                .price(transaction.getPrice().doubleValue())
                                                .date(transaction.getTimestamp())
                                                .build())
                                .collect(Collectors.toList());

                return UserResponseDTO.builder()
                                .id(user.getId())
                                .wallet(user.getWallet())
                                .meter(user.getMeter())
                                .name(user.getName())
                                .initialTransactionString(user.getInitialTransactionString())
                                .email(user.getEmail())
                                .buyingTransactions(buyingTransactions)
                                .sellingTransactions(sellingTransactions)
                                .build();
        }
}
