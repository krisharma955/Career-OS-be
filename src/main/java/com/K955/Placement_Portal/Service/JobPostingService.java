package com.K955.Placement_Portal.Service;

import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingResponse;
import com.K955.Placement_Portal.DTOs.JobPosting.UpdateJobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.UpdateJobPostingStatus;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import com.K955.Placement_Portal.Enums.JobType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobPostingService {
    JobPostingResponse createJobPosting(Long userId, @Valid JobPostingRequest request);

    JobPostingResponse getJobPosting(Long jobId);

    JobPostingResponse updateJobPostingById(Long jobId, @Valid UpdateJobPostingRequest request);

    JobPostingResponse updateJobPostingStatus(Long jobId, UpdateJobPostingStatus status);

    void deleteJobPosting(Long jobId);

    Page<JobPostingResponse> filterSearchJobPostings(String company, JobType jobType, JobPostingStatus jobPostingStatus, String search, Pageable pageable);

}
