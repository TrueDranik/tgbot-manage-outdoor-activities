package com.bot.sup.repository.specification;

import com.bot.sup.model.ActivityRequestParams;
import com.bot.sup.model.entity.Activity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ActivitySpecification implements Specification<Activity> {

    private final ActivityRequestParams params;

    public ActivitySpecification(ActivityRequestParams params) {
        this.params = params;
    }

    @Override
    public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (params.getActivityTypeId() != null) {
            predicates.add(
                    builder.equal(root.get("activityType"), params.getActivityTypeId())
            );
        }
        if (params.getActivityFormatId() != null) {
            predicates.add(
                    builder.equal(root.get("activityFormat"), params.getActivityFormatId())
            );
        }
        if (params.getRouteId() != null){
            predicates.add(
                    builder.equal(root.get("route"), params.getRouteId())
            );
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
