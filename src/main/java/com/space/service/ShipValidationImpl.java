package com.space.service;

import com.space.model.Ship;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class ShipValidationImpl implements ShipValidation{
    @Override
    public boolean createShipValidation(Ship ship) {
        boolean name = nameValidation(ship);
        boolean planet = planetValidation(ship);
        boolean speed = speedValidation(ship);
        boolean crewSize = crewSizeValidation(ship);
        boolean prodDate = prodDateValidation(ship);
        return name && planet && speed && crewSize && prodDate;
    }

    @Override
    public boolean idValidation(Long id) {
        return id > 0;
    }

    @Override
    public boolean nameValidation(Ship ship) {
        String name = ship.getName();
        return name.length() > 0 && name.length() < 51;
    }

    @Override
    public boolean planetValidation(Ship ship) {
        String planet = ship.getPlanet();
        return planet.length() > 0 && planet.length() < 51;
    }

    @Override
    public boolean speedValidation(Ship ship) {
        Double speed = ship.getSpeed();
        return speed > 0.00 && speed < 1.00;
    }

    @Override
    public boolean crewSizeValidation(Ship ship) {
        Integer crewSize = ship.getCrewSize();
        return crewSize > 0 && crewSize < 9999;
    }

    public boolean prodDateValidation(Ship ship) {
        Date prodDate = ship.getProdDate();
        Calendar calendarStart = new GregorianCalendar();
        calendarStart.set(Calendar.YEAR, 2799);
        Date startDate = new Date(calendarStart.getTimeInMillis());

        Calendar calendarEnd = new GregorianCalendar();
        calendarEnd.set(Calendar.YEAR, 3019);
        Date endDate = new Date(calendarEnd.getTimeInMillis());

        return prodDate.after(startDate) && prodDate.before(endDate);
    }
}
