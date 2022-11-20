package com.bot.sup.repository.specification;

import com.bot.sup.model.ScheduleRequestParams;
import com.bot.sup.model.entity.Activity;
import com.bot.sup.model.entity.Route;
import com.bot.sup.model.entity.Schedule;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleSpecification implements Specification<Schedule> {
    private final ScheduleRequestParams params;

    public ScheduleSpecification(ScheduleRequestParams params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<Schedule> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates  = new ArrayList<>();
        if (params.getActivityId() != null){
            predicates.add(
                    builder.equal(root.get("activity"), params.getActivityId())
            );
        }
        if (params.getRouteId() != null){
            Join<Schedule, Route> join = root.join("route");
            predicates.add(
                    builder.equal(join.get("id"), "id")
            );
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
