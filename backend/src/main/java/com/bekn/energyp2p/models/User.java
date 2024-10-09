package com.bekn.energyp2p.models;

import java.util.List;

import com.bekn.energyp2p.enums.EnergyExchangeCapabilityType;
import com.bekn.energyp2p.enums.IndustryType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Email
    @Column(unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    private EnergyExchangeCapabilityType energyExchangeCapability;

    private String initialTransactionString;

    @Enumerated(EnumType.STRING)
    private IndustryType industryType;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Meter meter;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<EnergyTransaction> buyingTransactions;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<EnergyTransaction> sellingTransactions;

    @ManyToOne(fetch = FetchType.LAZY)
    private Grid grid;
}
