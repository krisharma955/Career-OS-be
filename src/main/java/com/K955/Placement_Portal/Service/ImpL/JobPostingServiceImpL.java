package com.K955.Placement_Portal.Service.ImpL;

import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingResponse;
import com.K955.Placement_Portal.DTOs.JobPosting.UpdateJobPostingRequest;
import com.K955.Placement_Portal.Entity.Company;
import com.K955.Placement_Portal.Entity.JobPosting;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import com.K955.Placement_Portal.Exceptions.BadRequestException;
import com.K955.Placement_Portal.Exceptions.ResourceNotFoundException;
import com.K955.Placement_Portal.Mapper.JobPostingMapper;
import com.K955.Placement_Portal.Repository.CompanyRepository;
import com.K955.Placement_Portal.Repository.JobPostingRepository;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.JobPostingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JobPostingServiceImpL implements JobPostingService {

    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobPostingMapper jobPostingMapper;
    private final JwtUtil jwtUtil;

    @Override
    public JobPostingResponse createJobPosting(Long userId, JobPostingRequest request) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company", userId.toString()));

        JobPosting jobPosting = jobPostingMapper.toJobPosting(request);
        jobPosting.setCompany(company);
        jobPostingRepository.save(jobPosting);

        return jobPostingMapper.toJobPostingResponse(jobPosting);
    }

    @Override
    public JobPostingResponse getJobPostingById(Long jobId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", jobId.toString()));
        return jobPostingMapper.toJobPostingResponse(jobPosting);
    }

    @Override
    public List<JobPostingResponse> getAllJobPostings() {
        List<JobPosting> jobPostingList = jobPostingRepository.findAllByJobPostingStatus(JobPostingStatus.OPEN);
        return jobPostingMapper.toListOfJobPostingResponse(jobPostingList);
    }

    @Override
    public JobPostingResponse updateJobPostingById(Long jobId, UpdateJobPostingRequest request) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", jobId.toString()));

        securityCheck(jobPosting);

        if(request.title() != null) jobPosting.setTitle(request.title());
        if(request.description() != null) jobPosting.setDescription(request.description());
        if(request.location() != null) jobPosting.setLocation(request.location());
        if(request.minSalary() != null) jobPosting.setMinSalary(request.minSalary());
        if(request.maxSalary() != null) jobPosting.setMaxSalary(request.maxSalary());
        if(request.jobType() != null) jobPosting.setJobType(request.jobType());
        if(request.skills() != null) jobPosting.setRequiredSkills(request.skills());
        if(request.openings() != null) jobPosting.setOpenings(request.openings());
        if(request.deadline() != null) jobPosting.setDeadline(request.deadline());

        JobPosting saved = jobPostingRepository.save(jobPosting);

        return jobPostingMapper.toJobPostingResponse(saved);
    }

    @Override
    public void closeJobPosting(Long jobId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", jobId.toString()));
        securityCheck(jobPosting);
        jobPosting.setJobPostingStatus(JobPostingStatus.CLOSE);
        jobPostingRepository.save(jobPosting);
    }

    @Override
    public void deleteJobPosting(Long jobId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("JobPosting", jobId.toString()));
        securityCheck(jobPosting);
        jobPostingRepository.delete(jobPosting);
    }
    
    /// Util Methods

    private void securityCheck(JobPosting jobPosting) {
        Long userId = jwtUtil.getCurrentUserId();

        if(!jobPosting.getCompany().getUser().getId().equals(userId)) {
            throw new BadRequestException("Inaccessible Request!");
        }
    }
    
}
