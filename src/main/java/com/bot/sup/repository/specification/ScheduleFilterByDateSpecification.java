package com.bot.sup.repository.specification;

import com.bot.sup.model.ScheduleParams;
import com.bot.sup.model.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@RequiredArgsConstructor
public class ScheduleFilterByDateSpecification implements Specification<ScheduleDto> {
    private final ScheduleParams params;

    @Override
    public Predicate toPredicate(Root<ScheduleDto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        SpecificationBuilder<ScheduleDto> specificationBuilder = new SpecificationBuilder<>(root, query, builder);
        specificationBuilder.addGreaterThenOrEqualsDatePredicateIfNotNull("eventDate", params.getEventDate());
        specificationBuilder.addEqualsPredicateIfNotNull("isActive", params.getIsActive());

        return builder.and(specificationBuilder.build());
    }
}
