package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

public interface ShipSpecification {
    Specification<Ship> filterByName(String name);
    Specification<Ship> filterByPlanet(String planet);
    Specification<Ship> filterByShipType(ShipType shipType);
    Specification<Ship> filterByDate(Long from, Long to);
    Specification<Ship> filterByUsage(Boolean isUsed);
    Specification<Ship> filterBySpeed(Double min, Double max);
    Specification<Ship> filterByCrewSize(Integer min, Integer max);
    Specification<Ship> filterByRating(Double min, Double max);
}
