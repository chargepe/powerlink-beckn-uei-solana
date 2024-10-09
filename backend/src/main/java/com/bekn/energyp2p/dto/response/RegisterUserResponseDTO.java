package com.bekn.energyp2p.dto.response;

import com.bekn.energyp2p.dto.TransactionDTO;
import com.bekn.energyp2p.models.Meter;
import com.bekn.energyp2p.models.User;
import com.bekn.energyp2p.models.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserResponseDTO {
        private Long id;
        private String name;
        private String email;
        private Wallet wallet;
        private String initialTransactionString;
        private Meter meter;
        private List<TransactionDTO> buyingTransactions;
        private List<TransactionDTO> sellingTransactions;

        public static RegisterUserResponseDTO fromUser(User user) {
                if (user == null) {
                        throw new NullPointerException("User cannot be null");
                }

                List<TransactionDTO> buyingTransactions = user.getBuyingTransactions() == null ? List.of()
                                : user.getBuyingTransactions()
                                                .stream()
                                                .map(transaction -> TransactionDTO.builder()
                                                                .transactionAmount(transaction.getTransactionAmount())
                                                                .price(transaction.getPrice().doubleValue())
                                                                .date(transaction.getTimestamp())
                                                                .build())
                                                .toList();

                List<TransactionDTO> sellingTransactions = user.getSellingTransactions() == null ? List.of()
                                : user.getSellingTransactions()
                                                .stream()
                                                .map(transaction -> TransactionDTO.builder()
                                                                .transactionAmount(transaction.getTransactionAmount())
                                                                .price(transaction.getPrice().doubleValue())
                                                                .date(transaction.getTimestamp())
                                                                .build())
                                                .toList();

                return RegisterUserResponseDTO.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .email(user.getEmail())
                                .wallet(user.getWallet())
                                .initialTransactionString(user.getInitialTransactionString())
                                .meter(user.getMeter())
                                .buyingTransactions(buyingTransactions)
                                .sellingTransactions(sellingTransactions)
                                .build();
        }
}
