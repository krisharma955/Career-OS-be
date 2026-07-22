package com.K955.Placement_Portal.Repository;

import com.K955.Placement_Portal.Entity.AtsReport;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AtsReportSpecification {

    public static Specification<AtsReport> searchReport(
            String search,
            Long userId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("resume").get("student").get("user").get("id"), userId));

            if (search != null && !search.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("resume").get("fileName")), "%" + search.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
