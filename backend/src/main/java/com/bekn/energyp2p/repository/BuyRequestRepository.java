package com.bekn.energyp2p.repository;

import com.bekn.energyp2p.dto.response.DailyDemandDTO;
import com.bekn.energyp2p.enums.RequestStatus;
import com.bekn.energyp2p.models.BuyRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface BuyRequestRepository extends JpaRepository<BuyRequest, Long> {
        @Query("SELECT b FROM BuyRequest b WHERE b.status IN ('COMPLETE', 'PARTIALLY_COMPLETED', 'ORDER_PLACED', 'REJECTED') AND b.buyer.grid.id = :gridId")
        List<BuyRequest> findAllBuyRequestsByGrid(@Param("gridId") Long gridId);

        @Query("SELECT b FROM BuyRequest b WHERE b.maxPricePerUnit >= :pricePerUnit AND b.energyUnits <= :remainingUnits AND b.status = com.bekn.energyp2p.enums.RequestStatus.ORDER_PLACED")
        List<BuyRequest> findBuyRequestsByPriceAndUnits(@Param("pricePerUnit") Double pricePerUnit,
                        @Param("remainingUnits") Integer remainingUnits);

        List<BuyRequest> findAllByStatus(RequestStatus status);

        @Query("SELECT COALESCE(SUM(br.energyUnits), 0) FROM BuyRequest br WHERE br.buyer.grid.id = :gridId AND br.timeStamp BETWEEN :start AND :end")
        Long sumEnergyUnitsByGridAndDate(Long gridId, LocalDateTime start, LocalDateTime end);
}