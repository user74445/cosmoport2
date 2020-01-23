package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShipSpecificationImpl implements ShipSpecification {
    @Override
    public Specification<Ship> filterByName(String name) {
        return (Specification<Ship>) (r, cq, cb) ->
                name == null ? null : cb.like(r.get("name"), "%" + name + "%");
    }

    @Override
    public Specification<Ship> filterByPlanet(String planet) {
        return (Specification<Ship>) (r, cq, cb) ->
                planet == null ? null : cb.like(r.get("planet"), "%" + planet + "%");
    }

    @Override
    public Specification<Ship> filterByShipType(ShipType shipType) {
        return (Specification<Ship>) (r, cq, cb) ->
                shipType == null ? null : cb.equal(r.get("shipType"), shipType);
    }

    @Override
    public Specification<Ship> filterByDate(Long after, Long before) {
        return (Specification<Ship>) (r, cq, cb) ->
        {
            if (after == null && before == null) return null;
            if (after == null) {
                Date beforeDate = new Date(before-3600001);
                return cb.lessThanOrEqualTo(r.get("prodDate"), beforeDate);
            }
            if (before == null) {
                Date afterDate = new Date(after);
                return cb.greaterThanOrEqualTo(r.get("prodDate"), afterDate);
            }
            Date beforeDate = new Date(before-3600001);
            Date afterDate = new Date(after);
            return cb.between(r.get("prodDate"), afterDate, beforeDate);
        };
    }

    @Override
    public Specification<Ship> filterByUsage(Boolean isUsed) {
        return (Specification<Ship>) (r, cq, cb) -> {
            if (isUsed == null) return null;
            if (isUsed) return cb.isTrue(r.get("isUsed"));
            else return cb.isFalse(r.get("isUsed"));
        };
    }

    @Override
    public Specification<Ship> filterBySpeed(Double min, Double max) {
        return (Specification<Ship>) (r, cq, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(r.get("speed"), max);
            if (max == null) return cb.greaterThanOrEqualTo(r.get("speed"), min);
            else return cb.between(r.get("speed"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByCrewSize(Integer min, Integer max) {
        return (Specification<Ship>) (r, cq, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(r.get("crewSize"), max);
            if (max == null) return cb.greaterThanOrEqualTo(r.get("crewSize"), min);
            else return cb.between(r.get("crewSize"), min, max);
        };
    }

    @Override
    public Specification<Ship> filterByRating(Double min, Double max) {
        return (Specification<Ship>) (r, cq, cb) -> {
            if (min == null && max == null) return null;
            if (min == null) return cb.lessThanOrEqualTo(r.get("rating"), max);
            if (max == null) return cb.greaterThanOrEqualTo(r.get("rating"), min);
            else return cb.between(r.get("rating"), min, max);
        };
    }
}
