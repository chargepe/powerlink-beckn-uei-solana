package com.bekn.energyp2p.repository;

import com.bekn.energyp2p.dto.response.DailyGenerationDTO;
import com.bekn.energyp2p.enums.RequestStatus;
import com.bekn.energyp2p.models.SellRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface SellRequestRepository extends JpaRepository<SellRequest, Long> {
        @Query("SELECT s FROM SellRequest s WHERE s.status IN ('COMPLETE', 'PARTIALLY_COMPLETED', 'ORDER_PLACED', 'REJECTED') AND s.seller.grid.id = :gridId")
        List<SellRequest> findAllSellRequestsByGrid(@Param("gridId") Long gridId);

        @Query("SELECT s FROM SellRequest s WHERE s.pricePerUnit <= :maxPricePerUnit AND s.remainingUnits >= :energyUnits AND s.status = 'ORDER_PLACED'")
        List<SellRequest> findSellRequestsByPriceAndUnits(@Param("maxPricePerUnit") Double maxPricePerUnit,
                        @Param("energyUnits") Integer energyUnits);

        List<SellRequest> findAllByStatus(RequestStatus status);

        @Query("SELECT COALESCE(SUM(sr.energyUnits), 0) FROM SellRequest sr WHERE sr.seller.grid.id = :gridId AND sr.timeStamp BETWEEN :start AND :end")
        Long sumEnergyUnitsByGridAndDate(Long gridId, LocalDateTime start, LocalDateTime end);
}