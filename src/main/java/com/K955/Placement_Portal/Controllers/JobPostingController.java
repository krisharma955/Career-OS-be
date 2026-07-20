package com.K955.Placement_Portal.Controllers;

import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.JobPostingResponse;
import com.K955.Placement_Portal.DTOs.JobPosting.UpdateJobPostingRequest;
import com.K955.Placement_Portal.DTOs.JobPosting.UpdateJobPostingStatus;
import com.K955.Placement_Portal.Enums.JobPostingStatus;
import com.K955.Placement_Portal.Enums.JobType;
import com.K955.Placement_Portal.Security.JwtUtil;
import com.K955.Placement_Portal.Service.JobPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<JobPostingResponse> createJobPosting(@Valid @RequestBody JobPostingRequest request) {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPostingService.createJobPosting(userId, request));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobPostingResponse> getJobPosting(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobPostingService.getJobPosting(jobId));
    }

    @GetMapping
    public ResponseEntity<Page<JobPostingResponse>> filterSearchJobPostings(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) JobPostingStatus jobPostingStatus,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 8, sort = "postedAt", direction = Sort.Direction.DESC)Pageable pageable
            ) {
        return ResponseEntity.ok(jobPostingService.filterSearchJobPostings(
                company, jobType, jobPostingStatus, search, pageable
        ));
    }

    @PatchMapping("/{jobId}")
    public ResponseEntity<JobPostingResponse> updateJobPosting(@PathVariable Long jobId,
                                                               @Valid @RequestBody UpdateJobPostingRequest request) {
        return ResponseEntity.ok(jobPostingService.updateJobPostingById(jobId, request));
    }

    @PatchMapping("/{jobId}/status")
    public ResponseEntity<JobPostingResponse> updateJobPostingStatus(@PathVariable Long jobId,
                                                                     @Valid @RequestBody UpdateJobPostingStatus status) {
        return ResponseEntity.ok(jobPostingService.updateJobPostingStatus(jobId, status));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long jobId) {
        jobPostingService.deleteJobPosting(jobId);
        return ResponseEntity.noContent().build();
    }

}
