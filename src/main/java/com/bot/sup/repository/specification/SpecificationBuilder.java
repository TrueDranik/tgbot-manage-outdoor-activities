package com.bot.sup.repository.specification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class SpecificationBuilder<T> {

    private final Root<T> root;
    private final CriteriaQuery<?> query;
    private final CriteriaBuilder builder;
    List<Predicate> predicates = new ArrayList<>();

    public void addEqualsPredicateIfNotNull(String route, Object params) {
        if (params != null) {
            predicates.add(predicateEquals(route, params));
        }
    }

    public Predicate predicateEquals(String route, Object paramValue) {
        return builder.equal(root.get(route), paramValue);
    }

    public void addGreaterThenOrEqualsDatePredicateIfNotNull(String route, LocalDate params) {
        if (params != null) {
            predicates.add(predicateGreaterThenOrEqualDate(route, params));
        }
    }

    public Predicate predicateGreaterThenOrEqualDate(String route, LocalDate paramValue) {
        return builder.greaterThanOrEqualTo(root.get(route), paramValue);

    }

    public void addGreaterThenOrEqualsTimePredicateIfNotNull(String route, LocalTime params) {
        if (params != null) {
            predicates.add(predicateGreaterThenOrEqualTime(route, params));
        }
    }

    public Predicate predicateGreaterThenOrEqualTime(String route, LocalTime paramValue) {
        return builder.greaterThanOrEqualTo(root.get(route), paramValue);
    }

    Predicate[] build() {
        return predicates.toArray(new Predicate[0]);
    }
}
