package com.bekn.energyp2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bekn.energyp2p.enums.GridStatus;
import com.bekn.energyp2p.models.Grid;

import java.util.Optional;

@Repository
public interface GridRepository extends JpaRepository<Grid, Long> {
    Optional<Grid> findByStatus(GridStatus status);

}
