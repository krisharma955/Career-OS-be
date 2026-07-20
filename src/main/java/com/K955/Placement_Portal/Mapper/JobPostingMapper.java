package com.K955.Placement_Portal.Mapper;

import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingResponse;
import com.K955.Placement_Portal.Entity.JobPosting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobPostingMapper {

    @Mapping(source = "company.companyName", target = "companyName")
    @Mapping(source = "company.location", target = "companyLocation")
    JobPostingResponse toJobPostingResponse(JobPosting jobPosting);

    List<JobPostingResponse> toListOfJobPostingResponse(List<JobPosting> jobPostingList);

}
