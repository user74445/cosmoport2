package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ShipService {
    Page<Ship> getShipsList(Specification<Ship> shipSpecification, Pageable pageable);

    Ship createShip(Ship ship);

    Ship getShip(Long id);

    Ship updateShip(Ship ship, Long id);

    void deleteShip(Long id);

    boolean isShipExist(Long id);
}
