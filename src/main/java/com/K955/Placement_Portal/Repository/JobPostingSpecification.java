package com.K955.Placement_Portal.Repository;

import com.K955.Placement_Portal.Entity.JobPosting;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import com.K955.Placement_Portal.Enums.JobType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobPostingSpecification {

    public static Specification<JobPosting> filterBy(
            String company, JobType jobType, JobPostingStatus jobPostingStatus, String search
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (company != null && !company.isBlank()) predicates.add(cb.like(cb.lower(root.get("company").get("companyName")), "%" + company.toLowerCase() + "%"));
            if(jobType != null) predicates.add(cb.equal(root.get("jobType"), jobType));
            if(jobPostingStatus != null) predicates.add(cb.equal(root.get("jobPostingStatus"), jobPostingStatus));
            if (search != null && !search.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
