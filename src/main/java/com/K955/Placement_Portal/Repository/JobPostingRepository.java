package com.K955.Placement_Portal.Repository;

import com.K955.Placement_Portal.Entity.JobPosting;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long>, JpaSpecificationExecutor<JobPosting> {

    List<JobPosting> findByCompanyId(Long companyId);

    List<JobPosting> findAllByJobPostingStatus(JobPostingStatus status);

}
