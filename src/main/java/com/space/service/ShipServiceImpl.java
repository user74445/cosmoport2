package com.space.service;

import com.space.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class ShipServiceImpl implements ShipService{
    private ShipRepository shipRepository;

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Page<Ship> getShipsList(Specification<Ship> shipSpecification, Pageable pageable) {
        return shipRepository.findAll(shipSpecification, pageable);
    }

    @Override
    public Ship getShip(Long id) {
        if (shipRepository.findById(id).isPresent()) {
            return shipRepository.findById(id).get();
        } else {
            throw new ShipNotFoundException();
        }
    }

    @Override
    public Ship createShip(Ship ship) {
        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
        ship.setRating(getRating(ship));
        return shipRepository.saveAndFlush(ship);
    }

    @Override
    public Ship updateShip(Ship ship, Long id) {
        Ship editShip = shipRepository.findById(id).get();
        if (ship.getName() != null)
            editShip.setName(ship.getName());
        if (ship.getPlanet() != null)
            editShip.setPlanet(ship.getPlanet());
        if (ship.getShipType() != null)
            editShip.setShipType(ship.getShipType());
        if (ship.getProdDate() != null)
            editShip.setProdDate(ship.getProdDate());
        if (ship.getUsed() != null)
            editShip.setUsed(ship.getUsed());
        if (ship.getSpeed() != null)
            editShip.setSpeed(ship.getSpeed());
        if (ship.getCrewSize() != null)
            editShip.setCrewSize(ship.getCrewSize());
        editShip.setRating(getRating(editShip));
        return shipRepository.saveAndFlush(editShip);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }

    private static Double getRating(Ship ship) {
        double r, v, k;
        long y0, y1;
        v = ship.getSpeed();
        if (!ship.getUsed()) k = 1.0;
        else k = 0.5;
        y0 = 3019;
        y1 = getYear(ship.getProdDate());
        r = (80 * v * k) / (y0 - y1 + 1);
        return Math.round(r * 100.0) / 100.0;
    }

    private static int getYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    @Override
    public boolean isShipExist(Long id) {
        return shipRepository.existsById(id);
    }
}
