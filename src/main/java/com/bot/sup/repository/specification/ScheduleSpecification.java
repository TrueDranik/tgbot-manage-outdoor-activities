package com.bot.sup.repository.specification;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ScheduleSpecification implements Specification<ScheduleDto> {
    private final ScheduleRequestParams params;


    @Override
    public Predicate toPredicate(Root<ScheduleDto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        SpecificationBuilder<ScheduleDto> specificationBuilder = new SpecificationBuilder<>(root, query, builder);
        specificationBuilder.addEqualsPredicateIfNotNull("activity", params.getActivityId());
        specificationBuilder.addEqualsPredicateIfNotNull("route", params.getRouteId());

        return builder.and(specificationBuilder.build());
    }
}
