package com.bot.sup.repository.specification;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.dto.ScheduleDto;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ScheduleSpecification implements Specification<ScheduleDto> {
    private final ScheduleRequestParams params;

    public ScheduleSpecification(ScheduleRequestParams params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<ScheduleDto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (params.getActivityId() != null) {
            predicates.add(
                    builder.equal(root.get("activity"), params.getActivityId())
            );
        }
        if (params.getRouteId() != null) {
            predicates.add(
                    builder.equal(root.get("route"), params.getRouteId())
            );
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
