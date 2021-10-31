package com.desiredchargingstation.map.repository;

import com.desiredchargingstation.map.model.ChargingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingPointRepository extends JpaRepository<ChargingPoint, Long> {
}
