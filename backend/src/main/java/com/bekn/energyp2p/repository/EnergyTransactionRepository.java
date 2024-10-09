package com.bekn.energyp2p.repository;

import com.bekn.energyp2p.dto.response.DailyEnergyTradeDTO;
import com.bekn.energyp2p.models.EnergyTransaction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface EnergyTransactionRepository extends JpaRepository<EnergyTransaction, Long> {
    @Query("SELECT new com.bekn.energyp2p.dto.response.DailyEnergyTradeDTO(DATE(e.timestamp), SUM(e.transactionAmount), COUNT(e), SUM(e.price), AVG(e.price / e.transactionAmount), e.grid.id) "
            +
            "FROM EnergyTransaction e " +
            "WHERE e.status = 'SUCCESSFUL' AND e.grid.id = :gridId " +
            "GROUP BY DATE(e.timestamp), e.grid.id " +
            "ORDER BY DATE(e.timestamp) DESC")
    List<DailyEnergyTradeDTO> getDailyEnergyTradeDataByGrid(@Param("gridId") Long gridId);

    Optional<EnergyTransaction> findById(Long id);

    List<EnergyTransaction> findAllByGridId(Long id);

    @Query("SELECT COALESCE(SUM(t.transactionAmount), 0) FROM EnergyTransaction t WHERE t.grid.id = :gridId AND t.timestamp BETWEEN :start AND :end")
    Long sumTransactionAmount(Long gridId, LocalDateTime start, LocalDateTime end);
}
