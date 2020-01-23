package com.space.service;

import com.space.model.Ship;

public interface ShipValidation {
    boolean createShipValidation(Ship ship);
    boolean idValidation(Long id);
    boolean nameValidation(Ship ship);
    boolean planetValidation(Ship ship);
    boolean speedValidation(Ship ship);
    boolean crewSizeValidation(Ship ship);
    boolean prodDateValidation(Ship ship);
}
