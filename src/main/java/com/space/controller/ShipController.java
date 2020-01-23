package com.space.controller;

import com.space.exceptions.BadRequestException;
import com.space.exceptions.BadShipIdException;
import com.space.exceptions.ShipErrorResponse;
import com.space.exceptions.ShipNotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.ShipSpecification;
import com.space.service.ShipValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {
    private ShipService shipService;
    private ShipValidation shipValidation;
    private ShipSpecification shipSpecification;

    @Autowired
    public ShipController(ShipService shipService, ShipSpecification shipSpecification, ShipValidation shipValidation) {
        this.shipService = shipService;
        this.shipValidation = shipValidation;
        this.shipSpecification = shipSpecification;
    }

    @GetMapping("/ships")
    public List<Ship> getShipsList(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "planet", required = false) String planet,
                                   @RequestParam(value = "shipType", required = false) ShipType shipType,
                                   @RequestParam(value = "after", required = false) Long after,
                                   @RequestParam(value = "before", required = false) Long before,
                                   @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                   @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                   @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                   @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                   @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                   @RequestParam(value = "minRating", required = false) Double minRating,
                                   @RequestParam(value = "maxRating", required = false) Double maxRating,
                                   @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order,
                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return shipService.getShipsList(
                Specification.where(shipSpecification.filterByName(name)
                        .and(shipSpecification.filterByPlanet(planet)))
                        .and(shipSpecification.filterByShipType(shipType))
                        .and(shipSpecification.filterByDate(after, before))
                        .and(shipSpecification.filterByUsage(isUsed))
                        .and(shipSpecification.filterBySpeed(minSpeed, maxSpeed))
                        .and(shipSpecification.filterByCrewSize(minCrewSize, maxCrewSize))
                        .and(shipSpecification.filterByRating(minRating, maxRating)), pageable)
                .getContent();
    }

    @GetMapping("/ships/count")
    public Integer getShipsCount(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "planet", required = false) String planet,
                                 @RequestParam(value = "shipType", required = false) ShipType shipType,
                                 @RequestParam(value = "after", required = false) Long after,
                                 @RequestParam(value = "before", required = false) Long before,
                                 @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                 @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                 @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                 @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                 @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                 @RequestParam(value = "minRating", required = false) Double minRating,
                                 @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return shipService.getShipsList(
                Specification.where(shipSpecification.filterByName(name)
                        .and(shipSpecification.filterByPlanet(planet)))
                        .and(shipSpecification.filterByShipType(shipType))
                        .and(shipSpecification.filterByDate(after, before))
                        .and(shipSpecification.filterByUsage(isUsed))
                        .and(shipSpecification.filterBySpeed(minSpeed, maxSpeed))
                        .and(shipSpecification.filterByCrewSize(minCrewSize, maxCrewSize))
                        .and(shipSpecification.filterByRating(minRating, maxRating)), Pageable.unpaged()).getNumberOfElements();
    }

    @PostMapping("/ships")
    @ResponseBody
    public Ship createShip(@RequestBody Ship ship) {
        try {
            if (!shipValidation.createShipValidation(ship))
                throw new BadRequestException();
        } catch (NullPointerException e) {
            throw new BadRequestException();
        }
        ship.setId(null);
        return shipService.createShip(ship);
    }

    @GetMapping("/ships/{id}")
    @ResponseBody
    public Ship getShip(@PathVariable Long id) {
        if (!shipValidation.idValidation(id))
            throw new BadRequestException();
        if (!shipService.isShipExist(id))
            throw new ShipNotFoundException();
        return shipService.getShip(id);
    }

    @PostMapping("/ships/{id}")
    public Ship updateShip(@PathVariable Long id, @RequestBody Ship ship) {
        if (!shipValidation.idValidation(id))
            throw new BadRequestException();
        if (!shipService.isShipExist(id))
            throw new ShipNotFoundException();
        if (ship.getName() != null)
            if (!shipValidation.nameValidation(ship))
                throw new BadRequestException();
        if (ship.getPlanet() != null)
            if (!shipValidation.planetValidation(ship))
                throw new BadRequestException();
        if (ship.getProdDate() != null)
            if (!shipValidation.prodDateValidation(ship))
                throw new BadRequestException();
        if (ship.getSpeed() != null)
            if (!shipValidation.speedValidation(ship))
                throw new BadRequestException();
        if (ship.getCrewSize() != null)
            if (!shipValidation.crewSizeValidation(ship))
                throw new BadRequestException();
        return shipService.updateShip(ship, id);
    }

    @DeleteMapping("/ships/{id}")
    public void deleteShip(@PathVariable Long id) {
        if (!shipValidation.idValidation(id))
            throw new BadRequestException();
        if (!shipService.isShipExist(id))
            throw new ShipNotFoundException();
        shipService.deleteShip(id);
    }

    @ExceptionHandler
    public ResponseEntity<ShipErrorResponse> handleBadShipIdException(BadShipIdException exc) {
        ShipErrorResponse error = new ShipErrorResponse(HttpStatus.BAD_REQUEST.value(), "Не верный формат id");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ShipErrorResponse> handleShipNotFoundException(ShipNotFoundException exc) {
        ShipErrorResponse error = new ShipErrorResponse(HttpStatus.NOT_FOUND.value(), "Корабля с таким id не существует");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ShipErrorResponse> handleShipBadRequestException(BadRequestException exc) {
        ShipErrorResponse error = new ShipErrorResponse(HttpStatus.BAD_REQUEST.value(), "Что-то пошло не так");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
